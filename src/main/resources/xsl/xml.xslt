<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://entities.logsearch.example.com">

    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    <xsl:template match="/">
        <xsl:copy-of select="ns:logs"/>
    </xsl:template>

</xsl:stylesheet>