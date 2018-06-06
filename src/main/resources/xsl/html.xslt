<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://entities.logsearch.example.com">

    <xsl:output method="html" indent="yes" omit-xml-declaration="yes" encoding="utf-8"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>LogSearch Result</title>
            </head>
            <body>
                <div>
                    <h2>
                        Имя, Фамилия создателя, OOO «Siblion»
                    </h2>
                    <table border="1" align="right">
                        <tr bgcolor="#9acd32">
                            <th>Regular expression</th>
                            <th>Location</th>
                            <th>Date from</th>
                            <th>Date to</th>
                        </tr>
                        <xsl:for-each select="ns:logs/ns:searchInfo">
                            <tr>
                                <td>
                                    <xsl:value-of select="ns:regularExpression"/>
                                </td>
                                <td>
                                    <xsl:value-of select="ns:location"/>
                                </td>
                                <xsl:for-each select="ns:dateIntervals">
                                    <xsl:choose>
                                        <xsl:when test="position() = 1">
                                            <td>
                                                <xsl:value-of select="ns:dateFrom"/>
                                            </td>
                                            <td>
                                                <xsl:value-of select="ns:dateTo"/>
                                            </td>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <tr>
                                                <td/>
                                                <td/>
                                                <td>
                                                    <xsl:value-of select="ns:dateFrom"/>
                                                </td>
                                                <td>
                                                    <xsl:value-of select="ns:dateTo"/>
                                                </td>
                                            </tr>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:for-each>
                            </tr>
                        </xsl:for-each>
                    </table>
                </div>
                <div>
                    <table border="1" width="100%">
                        <caption>
                            <b>Search result</b>
                        </caption>
                        <tr bgcolor="#9acd32">
                            <th>Time moment</th>
                            <th>File name</th>
                            <th>Content</th>
                        </tr>
                        <xsl:for-each select="ns:logs/ns:searchInfoResult/ns:resultLogs">
                            <tr>
                                <td>
                                    <xsl:value-of select="ns:timeMoment"/>
                                </td>
                                <td>
                                    <xsl:value-of select="ns:fileName"/>
                                </td>
                                <td>
                                    <xsl:value-of select="ns:content"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                        <xsl:if test="count(ns:logs/ns:searchInfoResult/ns:resultLogs) = 0">
                            <tr>
                                <td colspan="3" align="center">
                                    <xsl:value-of select="ns:logs/ns:searchInfoResult/ns:emptyResultMessage"/>
                                </td>
                            </tr>
                        </xsl:if>
                    </table>
                </div>
                <p align="center">Created by LogSearch app</p>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>