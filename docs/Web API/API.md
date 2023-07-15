# PostChat - Api Docs

- This document contains information about API's Routes, Requests and Responses

- This document is organized by **Routes**
  
  <br>

## Register `/api/v*/register`

Register to the service

### Operations:

**POST**

* Request Body:
  
  ```json
  {
      "name" : "Test name",
      "phoneNumber": "912912912",
      "region": 351,
      "password": "Test Password 1",
  }
  ```

* Returns:
  
  ```
  Cookie(name=token, value=<tokenValue>  path=/api, isHttpOnly=true, secure=true, maxAge=604800)
  ```

<br>

## Login `/api/v*/login`

### Operations:

**POST** `- Login to the service`

* Request Body:
  
  ```json
  {
  "phoneNumber": "912912912",
  "region": 351,
  "password": "Test Password 1"
  }
  ```

* Returns:
  
  ```
  Cookie(name=token, value=<tokenValue>  path=/api, isHttpOnly=true, secure=true, maxAge=604800)
  //maxAge is in seconds and represents 7 days 
  ```

<br>

## Logout `/api/v*/logout`

### Operations:

**POST** `- Logout from the service`

* Empty request body

* Returns:
  
  ```
  Cookie(name=token, value=""  path=/api, isHttpOnly=true, secure=true, maxAge=604800)
  ```
  
  <br>

---

> **Warning**  
> All operations for `/api/v*/user...` require a valid token cookie

---

<br>

## User `/api/v*/user`

### Operations:

**GET** `- Get all users in service from phone contacts`

* Query parameters:
  ``
  ?phoneNumbersHashed=[bas64_hashed_number1, base64_hashed_number2]
  ``
  
  > **Note**  
  > This query parameter is obligatorily  
  > Phone numbers consist of region and number  

* Response body:
  
  ```json
  {
      "list": [
        {
          "phoneNumber": "351912345671", 
          "name": "test_user1"
        },
        {
          "phoneNumber": "351912345672", 
          "name": "test_user2"
        }
      ]
  }
  ```

<br>

**DELETE** `- remove user from the service`

<br>

## User `/api/v*/user/{phoneNumber}`

### Operations:

**GET** `- Get your information`

- Response Body:

```json
{
  "phoneNumber": "351912345676",
  "name": "test_user2",
}
```

<br>

## Chat `/api/v*/message`

### Operations:

**GET** - `Returns all messages`

* Return Body:

```json
{
  "list": [
    {
      "id": 123,
      "userFrom": "user_from_hashed_number",
      "chatTo": 1,
      "mergedContent" : "base64_encoded_merged_image" 
      "handwrittenContent": "base64_encoded_image",
      "templateName": "template_unique_name",
      "createdMessageAt": "2040-05-09 00:11:12.908501"
    }
  ]
}
```

<br>

## Chat `/api/v*/chat`

### Operations:

**POST** - `Create a chat`

- Request Body:
  
  ```json
  {
      "phoneNumbers" : [
          "35191235678",
          "35191235679"        
      ],
      "name" : "My Chat",
      "createdAt" : "2040-05-09 00:11:12.908501"
  }
  ```
* Response Body:
  
  ```json
  {
      "id" : 1,
      "name" : "My Chat",
      "createdAt" : "2040-05-09 00:11:12.908501",
      "lastMessage": "2040-05-09 00:11:12.908501"
  }
  ```

> **Note**: lastMessage may not exist.

    

**GET** - `Get all chats`

- Response Body:
  
  ```json
  {
      "list": [
          {
              "id" : 1,
              "name" : "My Chat",
              "createdAt" : "2040-05-09 00:11:12.908501".
              "lastMessage": "2040-05-09 00:11:12.908501"
          },
          {
              "id" : 2,
              "name" : "My Chat",
              "createdAt" : "2040-05-09 00:11:12.908501"
          },
      ]
  }
  ```

## 

## Chat `/api/v*/chat/{id}`

### Operations:

**POST** - `Send a postcard`

* Request Body:
  
  ```json
  {
      "content": "base64_svg",
      "templateName": "name_of_the_template",
      "createdAt" : "2040-05-09 00:11:12.908501"
  }
  ```

* Response Body: 
  
  ```json
  {
       "id": 123,
       "userFrom": "user_from_hashed_number",
       "chatTo": 1,
       "mergedContent" : "base64_encoded_merged_image" 
       "handwrittenContent": "base64_encoded_image",
       "templateName": "template_unique_name",
       "createdMessageAt": "2040-05-09 00:11:12.908501"
  }
  ```

**GET** - `Get chat info`

* Response Body:
  
  ```json
  {
      "props": {
           "id": 123,
           "nme": "chat_name",
           "createdAt": "2040-05-09 00:11:12.908501"
      },
      "usersInfo": [
            {
              "id": 9998,
              "phoneNumberHashed": "phone_number_hashed2",
              "name": "test_user2",
              "bio": "test2"
            },
            {
              "id": 9997,
              "phoneNumberHashed": "phone_number_hashed1",
              "name": "test_user1",
              "bio": "test1"
            }
       ]
  }
  ```

<br>

## Message `/api/v*/message`

### Operations:

**GET** - `Get all pending messages`

- Response Body:
  
  ```json
  {
      "list": [
          {
              "id" : 1,
              "userFrom" : "351912345678",
              "chatTo" : 1 ,
              "mergedContent" : "base64_merged_svg_content",
              "handwrittenContent" : "base64_handwritten_svg_content",
              "templateName" : "name_of_the_template",
              "createdMessageAt" : "2040-05-09 00:11:12.908501",
          },
  
          {
              "id" : 2,
              "userFrom" : "351912345678",
              "chatTo" : 1 ,
              "mergedContent" : "base64_merged_svg_content",
              "handwrittenContent" : "base64_handwritten_svg_content",
              "templateName" : "name_of_the_template",
              "createdMessageAt" : "2040-05-09 00:11:12.908501",
          }
      ]
  }
  ```

## Template `/api/v*/template`

### Operations:

**GET** - `Get all templates`

* Query parameters:
  ` ?gotten=[template_name1, template_name2]`

> **Note**
> 
> Gotten parameter is optional and represents the templates a client already has 

* Response Body:
  
  ```json
  {
      "list": [
          {
              "name" : "template_name1",
              "content" : "base64_svg_template1",
          },
          {
              "name" : "template_name2",
              "content" : "base64_svg_template2",
          },
      ]
  }
  ```

## HTR  - `/api/v*/htr`

#### Operations:

**POST** - `HTR a message`

* Request Body:

```json
{
    "handwrittenContent": "base64_handwritten_content"
}
```

* Response Body:

```json
{
    "text": "The result of the HTR operation"
}
```
