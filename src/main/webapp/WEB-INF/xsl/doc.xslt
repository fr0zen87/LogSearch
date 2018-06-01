<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:w="http://schemas.microsoft.com/office/word/2003/wordml"
                xmlns:ns="http://entities.logsearch.example.com">

    <xsl:template match="/">
        <w:wordDocument>
            <w:styles>
                <w:style w:type="paragraph" w:styleId="Heading3">
                    <w:name w:val="heading 3"/>
                    <w:pPr>
                        <w:pStyle w:val="Heading3"/>
                        <w:keepNext/>
                        <w:spacing w:before="240" w:after="60"/>
                        <w:outlineLvl w:val="2"/>
                    </w:pPr>
                </w:style>
            </w:styles>
            <w:body>
                <w:p>
                    <w:r>
                        <w:t>Created by: <xsl:value-of select="ns:logs/ns:creator"/></w:t>
                    </w:r>
                </w:p>
                <w:p/>
                <w:p>
                    <w:pPr>
                        <w:pStyle w:val="Heading3"/>
                    </w:pPr>
                    <w:r>
                        <w:t>Search info</w:t>
                    </w:r>
                </w:p>
                <w:p/>
                <w:p>
                    <w:r>
                        <w:t>Regular expression: <xsl:value-of select="ns:logs/ns:searchInfo/ns:regularExpression"/></w:t>
                    </w:r>
                </w:p>
                <w:p>
                    <w:r>
                        <w:t>Location: <xsl:value-of select="ns:logs/ns:searchInfo/ns:location"/></w:t>
                    </w:r>
                </w:p>
                <w:p>
                    <w:r>
                        <w:t>Date intervals:</w:t>
                    </w:r>
                </w:p>
                <xsl:for-each select="ns:logs/ns:searchInfo/ns:dateIntervals">
                    <w:p>
                        <w:r>
                            <w:t>from:   <xsl:value-of select="ns:dateFrom"/>, to:   <xsl:value-of select="ns:dateTo"/></w:t>
                        </w:r>
                    </w:p>
                </xsl:for-each>
                <w:p/>
                <w:p>
                    <w:pPr>
                        <w:pStyle w:val="Heading3"/>
                    </w:pPr>
                    <w:r>
                        <w:t>Search result</w:t>
                    </w:r>
                </w:p>
                <w:p/>
                <xsl:for-each select="ns:logs/ns:searchInfoResult/ns:resultLogs">
                    <w:p>
                        <w:r>
                            <w:t>Time moment:       <xsl:value-of select="ns:timeMoment"/></w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:r>
                            <w:t>File name:     <xsl:value-of select="ns:fileName"/></w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:r>
                            <w:t>Content:       <xsl:value-of select="ns:content"/></w:t>
                        </w:r>
                    </w:p>
                    <w:p/>
                </xsl:for-each>
                <xsl:if test="count(ns:logs/ns:searchInfoResult/ns:resultLogs) = 0">
                    <w:p>
                        <w:r>
                            <w:t><xsl:value-of select="ns:logs/ns:searchInfoResult/ns:emptyResultMessage"/></w:t>
                        </w:r>
                    </w:p>
                </xsl:if>
            </w:body>
        </w:wordDocument>
    </xsl:template>

</xsl:stylesheet>