# LogSearch app
Приложение для интеллектуального поиска логов на сервере приложений Weblogic.  

#### Ключевые элементы:  
```
1. regularExpression - регулярное выражение для поиска;  
2. dateIntervals - список временных интервалов, в которых искать. Если не заданы, то принимается расчет от -∞ до +∞;  
3. location - расположение .log файлов в файловой системе, в которых искать логи;  
4. realization - аттрибут для сохранения найденных логов в файл (true - сохранять, false - нет);  
5. fileExtension - расширение файла для сохранения (DOC, PDF, RTF, HTML, LOG, XML).  
```
Note: запущенное на сервере приложений Weblogic приложение автоматически построит путь до папки domains и ввести нужно оставшуюся 
часть пути (пример, location:"\\base_domain\\servers\\AdminServer"). В противном случае путь следует указывать как в примерах ниже,
начиная с корневого каталога. Поиск логов осуществляется только по стандартному шаблону логов Weblogic сервера.  
## Swagger UI
Пример обращения: http://localhost:8888//swagger-ui.html
***
## WEB
Пример обращения: http://localhost:8080/logSearch
***
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
	"location":"C:\\Users\\User\\domains\\logs",
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
            <ls:location>C:\\Users\\User\\domains</ls:location>
            <ls:realization>true</ls:realization>
            <ls:fileExtension>RTF</ls:fileExtension>
        </ls:searchInfo>
    </soapenv:Body>
</soapenv:Envelope>
```
