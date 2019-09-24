# ** Delete Task By ID **

This API will allow you to delete task by id

## URL

|||
| ---------------- | :--------------------------------------------------------------: |
|** Method **      |DELETE                                                            |
|** Structure **   |`/api/task/:id`                                                   |
|** Example **     |`/api/task/5d6951d52a7b7d5aaec993bc`                              |

## URI Params

|Key                  |Value                   |Required      |Description |
| ------------------- | :--------------------: | :----------: | ---------- |
| task_id             | UUID                   |  true        |            |

## Success Response
```json
{
    "data": {
        "id": "5d6951d52a7b7d5aaec993bc",
        "subject": "subject2",
        "detail": "detail2",
        "status": 0,
        "create_at": "Fri Aug 30 09:53:35 ICT 2019",
        "update_at": "Fri Aug 30 09:53:35 ICT 2019"
    },
    "message": "deleted",
    "code": 200
}
```

## Empty Response
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
|404          |	Not Found â€“ The specified could not be found.                               |
|500          |	Internal Server Error â€“ We had a problem with our server. Try again later.  |
