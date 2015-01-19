endpoint: `grababouquet.com`

### 1. Check ordrer details
`GET grababouquet.com/delivery/check` 

Params:
  - `order_number`: INT of 9 digits
  
Response:

````
{
  "order": {
      "id": 5,
      "number": "R050871489",
      "special_instructions": null,
      "ship_address": {
          "firstname": "Keang",
          "lastname": "Song",
          "address1": "#02-353",
          "address2": "456 Awesome Street",
          "zipcode": "32472",
          "phone": "94823406"
      }
  }
}
````

### 2. Send GPS coordinates

`POST grababouquet.com/delivery/track`

Params: 
  - `order_number`: INT of 9 digits
  - `lng`: FLOAT
  - `lat`: FLOAT

Response:

````
{ success: true}
````
or
```` 
{ error: error_message }
````
