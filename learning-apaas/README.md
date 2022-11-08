动态对象模型：
```json
{
  "id": 123456,
  "name": "demo_obj",
  "dataType": "mysql",
  "primaryKey": "id",
  "fields": [{
    "id": 111,
    "name": "id",
    "isPk": true,
    "type": "string",
    "default": "null"
  }, {
    "id": 112,
    "name": "name",
    "isPk": false,
    "type": "string",
    "default": "null"
  }, {
    "id": 113,
    "name": "user",
    "isPk": false,
    "type": "object",
    "object_id": 123455,
    "default": "null"
  }],
  "methods": [{
    "id": 123,
    "name": "get",
    "params": ["id"],
    "result_obj": 123
  },{
    "id": 234,
    "name": "put",
    "params": ["id", "name"],
    "result_obj": 123
  }]
}
```

type:
- string
- int
- long
- object
- float
- double


DynamicObject
- EntityObject
- FieldObject
- MethodObject
  - ParameterObject
  - ReturnObject

HttpDynamicObject
- url
- method
- body
- dynamic_obj_id

RpcDynamicObject


LazyDynamicObject
