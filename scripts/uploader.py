from clint.textui.progress import Bar
import errno
import json
from pprint import pprint
import requests
from requests_toolbelt import MultipartEncoder, MultipartEncoderMonitor
import shutil
import tempfile
import urllib3
import os

output_file_name = 'uploader.json'
excluded_items = ('uploader.json')
excluded_ext = ('.zip', '.py')
verify_certs = False
api_endpoint = 'http://localhost:8080'

putfile_method = api_endpoint + '/putFile'
download_method = api_endpoint + '/download'
if not verify_certs:
  urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

def create_monitor_callback(encoder):
  progress_bar = Bar(expected_size=encoder.len, filled_char='=')
  def callback(monitor):
    progress_bar.show(monitor.bytes_read)
  return callback

all_items = os.listdir('.')
items = list(filter(lambda x: not x.endswith(excluded_ext) and x not in excluded_items, all_items))
print('Found', len(items), 'items:', items)

uploaded_zips = []
for index, item in enumerate(items, start=1):
  print('Compressing', item, '...')
  if os.path.isfile(item):
    with tempfile.TemporaryDirectory() as tmp_dir:
      shutil.copy(item, tmp_dir)
      shutil.make_archive(item, 'zip', tmp_dir)
  else:
    shutil.make_archive(item, 'zip', item)
  zip_name = item + '.zip'
  zip_size_mb = os.path.getsize(zip_name) / (1024 * 1024)
  print('Uploading archive: {}. Size: {:,.5f} MB'.format(zip_name, zip_size_mb))
  try:
    with open(zip_name, 'rb') as zip_file:
      multipart_data = MultipartEncoder(
        fields={
          'file': (zip_name, zip_file)
        }
      )
      monitor_callback = create_monitor_callback(multipart_data)
      multipart_data_monitor = MultipartEncoderMonitor(multipart_data, monitor_callback)
      result = requests.post(putfile_method, data=multipart_data_monitor, headers={'Content-Type': multipart_data_monitor.content_type}, timeout=5.0, verify=verify_certs).json()
  except requests.exceptions.ConnectionError as e:
    print(e)
    print('Error while uploading {}. Skipping ({}/{})'.format(zip_name, index, len(items)))
    uploaded_zips.append({'name': zip_name, 'download': None, 'error': str(e)})
  else:
    id = result['id']['value']
    uploaded_zips.append({'name': zip_name, 'download': download_method + '/' + id})
    print('Uploaded {}. Id: {} ({}/{})'.format(zip_name, id, index, len(items)))
  finally:
    os.remove(zip_name)

pprint(uploaded_zips)
with open(output_file_name, 'w') as output_file:
  json.dump(uploaded_zips, output_file, indent=4)
print('All items processed. Output:', output_file_name)
