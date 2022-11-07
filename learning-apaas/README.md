DynamicObject
LazyDynamicObject
```json
{
  "name": "demo_obj",
  "dataType": "mysql",
  "primaryKey": "id",
  "fields": [{
    "name": "id",
    "isPk": true,
    "type": "string",
    "default": "null"
  }, {
    "name": "name"
  }],
  "subObj": "demo_sub_obj",
  "methods": [{
    "name": "get",
    "params": ["id"]
  },{
    "name": "put",
    "params": ["id", "name"]
  }]
}
```



