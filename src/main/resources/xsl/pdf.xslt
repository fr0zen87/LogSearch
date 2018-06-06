<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:ns="http://entities.logsearch.example.com">

    <xsl:output encoding="UTF-8" indent="yes" method="xml" standalone="no" omit-xml-declaration="no"/>
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-portrait" page-height="29.7cm" page-width="21.0cm" margin="2cm">
                    <fo:region-body/>
                    <fo:region-after/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-portrait">
                <fo:static-content flow-name="xsl-region-after" font-family="Arial">
                    <fo:block id="end-of-document">
                        <fo:block font-size="6px" space-before="6mm" text-align="right">Page
                            <fo:page-number/>
                        </fo:block>
                        <fo:block font-size="6px">
                            <xsl:value-of select="ns:logs/ns:application"/>
                        </fo:block>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body" font-family="Arial">
                    <fo:block margin-bottom="10mm">
                        Created by:
                        <xsl:value-of select="ns:logs/ns:creator"/>
                    </fo:block>
                    <fo:block font-size="20px" font-weight="bold" color="red">Search Info</fo:block>
                    <fo:block>Regular expression:
                        <xsl:value-of select="ns:logs/ns:searchInfo/ns:regularExpression"/>
                    </fo:block>
                    <fo:block>Location:
                        <xsl:value-of select="ns:logs/ns:searchInfo/ns:location"/>
                    </fo:block>
                    <fo:block>Date intervals:</fo:block>
                    <xsl:for-each select="ns:logs/ns:searchInfo/ns:dateIntervals">
                        <fo:block>
                            from:
                            <xsl:value-of select="ns:dateFrom"/> to:
                            <xsl:value-of select="ns:dateTo"/>
                        </fo:block>
                    </xsl:for-each>
                    <fo:block font-size="20px" font-weight="bold" color="red" margin-top="10mm">Search result</fo:block>
                    <xsl:for-each select="ns:logs/ns:searchInfoResult/ns:resultLogs">
                        <fo:block font-size="10px">
                            Time moment:
                            <xsl:value-of select="ns:timeMoment"/>
                        </fo:block>
                        <fo:block font-size="10px">
                            File name:
                            <xsl:value-of select="ns:fileName"/>
                        </fo:block>
                        <fo:block margin-bottom="5mm" font-size="10px">
                            Context:
                            <xsl:value-of select="ns:content"/>
                        </fo:block>
                    </xsl:for-each>
                    <xsl:if test="count(ns:logs/ns:searchInfoResult/ns:resultLogs) = 0">
                        <fo:block>
                            <xsl:value-of select="ns:logs/ns:searchInfoResult/ns:emptyResultMessage"/>
                        </fo:block>
                    </xsl:if>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>