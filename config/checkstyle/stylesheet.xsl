<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes"/>
    <xsl:decimal-format decimal-separator="." grouping-separator="," />
    <xsl:template match="checkstyle">
        <html>
            <head>
                <title>Checkstyle Report</title>
                <style type="text/css">
                    body {
                    margin-left: 10;
                    margin-right: 10;
                    font:normal .925em arial,helvetica,sanserif;
                    }
                    table {
                    font:normal .925em arial,helvetica,sanserif;
                    }

                </style>
            </head>
            <body>
                <!-- jakarta logo -->
                <h1>Checkstyle Report</h1>
                <hr size="1"/>

                <!-- Summary part -->
                <xsl:apply-templates select="." mode="summary"/>
                <hr size="1" width="100%" align="left"/>

                <!-- For each package create its part -->
                <xsl:for-each select="file[error]">
                    <xsl:sort select="@name"/>
                    <xsl:apply-templates select="."/>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="file">
        <h3><xsl:value-of select="@name"/></h3>
        <table style="border-spacing: 5px 0.20rem;">
            <xsl:for-each select="error">
                <tr>
                    <td><b>Line <xsl:value-of select="@line"/>:</b></td>
                    <td style="color:crimson"><xsl:value-of select="@message"/></td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>


    <xsl:template match="checkstyle" mode="summary">
        <h3>Summary</h3>
        <table>
            <tr>
                <td style="width:140px">
                    Total files checked
                </td>
                <td style="text-align:right">
                    <xsl:number level="any" value="count(descendant::file)"/>
                </td>
            </tr>
            <tr>
                <td style="width:140px">
                    Files with errors
                </td>
                <td style="text-align:right">
                    <xsl:number level="any" value="count(descendant::file[error])"/>
                </td>
            </tr>
            <tr>
                <td style="width:140px">
                    Total errors
                </td>
                <td style="text-align:right">
                    <xsl:number level="any" value="count(descendant::error)"/>
                </td>
            </tr>
        </table>
    </xsl:template>

</xsl:stylesheet>