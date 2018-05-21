<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://entities.logsearch.example.com">

    <xsl:output method="text" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
    <xsl:template match="/">
        Имя, Фамилия создателя, OOO «Siblion»

        SEARCH INFO:
        Regular expression: <xsl:value-of select="ns:logs/ns:searchInfo/ns:regularExpression"/>
        Location: <xsl:value-of select="ns:logs/ns:searchInfo/ns:location"/>
        Date intervals:<xsl:for-each select="ns:logs/ns:searchInfo/ns:dateIntervals">

            Interval:   Date from: <xsl:value-of select="ns:dateFrom"/>
                        Date to:   <xsl:value-of select="ns:dateTo"/>
        </xsl:for-each>

        SEARCH RESULT:<xsl:for-each select="ns:logs/ns:searchInfoResult/ns:resultLogs">

            Time moment: <xsl:value-of select="ns:timeMoment"/>
            File name:   <xsl:value-of select="ns:fileName"/>
            Content:     <xsl:value-of select="ns:content"/>
        </xsl:for-each>

        <xsl:if test="count(ns:logs/ns:searchInfoResult/ns:resultLogs) = 0">
            <xsl:value-of select="ns:logs/ns:searchInfoResult/ns:emptyResultMessage"/>
        </xsl:if>

        Created by LogSearch app
    </xsl:template>

</xsl:stylesheet>