# ** Create Task **

This API will allow you to create task for todo list

## URL

|||
| ---------------- | :--------------------------------------------------------------: |
|** Method **      |POST                                                              |
|** Structure **   |`/api/task`                                                       |
|** Example **     |`/api/task`                                                       |

## Header Params

|Key                  |Value                   |Required      |Description |
| ------------------- | :--------------------: | :----------: | ---------- |
| Content-Type        | application/json       |  true        |            |

## Body Params

|Key                  |type                    |Required      |Description |
| ------------------- | :--------------------: | :----------: | ---------- |
|subject              | String                 |true          |            |
|detail               | String                 |true          |            |

## Request Example
```json
{
	"subject": "subject3",
	"detail": "detail3"
}
```

## Success Response
```json
{
    "data": {
        "id": "5d6951d52a7b7d5aaec993bc",
        "subject": "subject3",
        "detail": "detail3",
        "status": 0,
        "create_at": "Fri Aug 30 11:11:49 ICT 2019",
        "update_at": "Fri Aug 30 11:11:49 ICT 2019"
    },
    "code": 200
}
```

## Error Response
```json
{
    "error": {
        "message": "Bad Request",
        "code": 10002
    },
    "code": 400
}
```

|Error Code   |Meaning                                                                        |
| ----------- | :-------------------------------------------------------------------------:   |
|400          |	Bad Request â€“ Your request worse.                                           |
|404          |	Not Found â€“ The specified could not be found.                               |
|500          |	Internal Server Error â€“ We had a problem with our server. Try again later.  |
