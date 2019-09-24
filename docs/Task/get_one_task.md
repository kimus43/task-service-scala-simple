# ** Get Task By ID **

This API will allow you to get task by ID

## URL

|||
| ---------------- | :--------------------------------------------------------------: |
|** Method **      |GET                                                               |
|** Structure **   |`/api/task/:id`                                                   |
|** Example **     |`/api/task/5d69523f2a7b7d5aaec993be`                              |

## URI Params

|Key                  |Value                   |Required      |Description |
| ------------------- | :--------------------: | :----------: | ---------- |
| task_id             | UUID                   |  true        |            |


## Success Response
```json
{
    "data": {
        "id": "5d69523f2a7b7d5aaec993be",
        "subject": "new Subject1",
        "detail": "detail1",
        "status": 0,
        "create_at": "Fri Aug 30 09:53:11 ICT 2019",
        "update_at": "Fri Aug 30 11:09:09 ICT 2019"
    },
    "code": 200
}
```

## Error Response
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
