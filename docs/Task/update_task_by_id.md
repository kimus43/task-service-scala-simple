# ** Update Task By ID**

This API will allow you to update subject, detail and status of task.

## URL

|||
| ---------------- | :--------------------------------------------------------------: |
|** Method **      |PUT                                                               |
|** Structure **   |`/api/task/:id`                                                   |
|** Example **     |`/api/task/5d69523f2a7b7d5aaec993be`                              |

## Header Params

|Key                  |Value                   |Required      |Description |
| ------------------- | :--------------------: | :----------: | ---------- |
| Content-Type        | application/json       |  true        |            |

## Body Params

|Key                  |type                    |Required      |Description |
| ------------------- | :--------------------: | :----------: | ---------- |
|subject              | String                 |true          |            |
|detail               | String                 |true          |            |
|status               | String/Integer         |true          |only(0 = pending, 1 = done)  |

**body params must have at least one param.
 
## Request Example
```json
{
    "subject": "Wow subject",
    "detail": "Wow detail",
    "status": 0
}
```

## Success Response
```json
{
    "data": {
        "id": "5d69523f2a7b7d5aaec993be"
    },
    "message": "updated",
    "code": 200
}
```

# Error Response
## Bad Request Response
```json
{
    "error": {
        "message": "require: [subject, detail, status]",
        "code": 10002
    },
    "code": 400
}
```

## Not Found Response
```json
{
    "error": {
        "message": "Not Found",
        "code": 10003
    },
    "code": 404
}
```

|Error Code   |Meaning                                                                        |
| ----------- | :-------------------------------------------------------------------------:   |
|400          |	Bad Request â€“ Your request worse.                                           |
|404          |	Not Found â€“ The specified could not be found.                               |
|500          |	Internal Server Error â€“ We had a problem with our server. Try again later.  |
