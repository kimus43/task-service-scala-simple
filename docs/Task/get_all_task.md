# ** Get All Task **

This API will allow you to get all task

## URL

|||
| ---------------- | :--------------------------------------------------------------: |
|** Method **      |GET                                                               |
|** Structure **   |`/api/task`                                                       |
|** Example **     |`/api/task `                                                      |


## Success Response
```json
{
    "data": [
        {
            "id": "5d6951d52a7b7d5aaec993bc",
            "subject": "Wow subject",
            "detail": "Wow detail",
            "status": 0,
            "create_at": "Fri Aug 30 09:53:11 ICT 2019",
            "update_at": "Fri Aug 30 11:21:27 ICT 2019"
        },
        {
            "id": "5d69523f2a7b7d5aaec993be",
            "subject": "subject3",
            "detail": "detail3",
            "status": 0,
            "create_at": "Fri Aug 30 11:11:49 ICT 2019",
            "update_at": "Fri Aug 30 11:11:49 ICT 2019"
        }
    ],
    "code": 200
}
```

## Empty Response
```json
{
    "data": [],
    "code": 200
}
```

|Error Code   |Meaning                                                                        |
| ----------- | :-------------------------------------------------------------------------:   |
|500          |	Internal Server Error â€“ We had a problem with our server. Try again later.  |
