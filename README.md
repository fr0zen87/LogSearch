# LogSearch app

## Swagger UI
Пример обращения: http://localhost:8888//swagger-ui.html
***
Note: запущенное на сервере приложений Weblogic приложение автоматически построит путь до папки domains и ввести нужно оставшуюся 
часть пути (пример, location:"\\base_domain\\servers\\AdminServer"). В противном случае путь следует указывать как в примерах ниже,
начиная с корневого каталога.  
## REST
Пример обращения: http://localhost:8080/rest/restLogSearch  
```
{
	"regularExpression":"Info",
	"dateIntervals":
	[
		{
			"dateFrom":"2018-03-15T00:00:00",
			"dateTo":"2018-04-01T00:00:00"
		}
	],
	"location":"C:\\Users\\User\\IdeaProjects\\domains\\logs",
	"realization":"true",
	"fileExtension":"DOC"
}
```
***
## SOAP
Пример обращения: http://localhost:8080/soap/soapLogSearch.wsdl<br>
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ls="http://entities.logsearch.example.com">
    <soapenv:Header/>
    <soapenv:Body>
        <ls:searchInfo>
            <ls:regularExpression></ls:regularExpression>
            <ls:dateIntervals>
                <ls:dateFrom>2018-01-09T10:15:30</ls:dateFrom>
                <ls:dateTo>2018-08-09T10:15:30</ls:dateTo>
            </ls:dateIntervals>
            <ls:dateIntervals>
                	<ls:dateFrom>2018-03-09T10:15:30</ls:dateFrom>
                	<ls:dateTo>2018-04-10T10:15:30</ls:dateTo>
            </ls:dateIntervals>
            <ls:location>C:\\Users\\APZhukov\\IdeaProjects\\domains</ls:location>
            <ls:realization>true</ls:realization>
            <ls:fileExtension>RTF</ls:fileExtension>
        </ls:searchInfo>
    </soapenv:Body>
</soapenv:Envelope>
```
***
## WEB
Пример обращения: http://localhost:8080/logSearch
