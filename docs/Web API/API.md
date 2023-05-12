# PostChat Api

- This document contains information about API's Routes, Requests and Responses
- This document is organized by **Routes**

  <br>

## Register `/api/register`

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
  "bio": "Something to say"
  }
  ```

  > **Note**  
  > Parameter "bio" is not obligatory

* Returns:

  ```
  Cookie(name=token, value=<tokenValue>  path=/api, isHttpOnly=true, secure=true, maxAge=604800)
  ```

<br>

## Login `/api/login`

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

## Logout `/api/logout`

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
> All operations for `/api/user...` require a valid token cookie

---

<br>

## User `/api/user`

### Operations:

**GET** `- Get all users in service from phone contacts`

* Query parameters:
  ``
  ?phoneNumbersHashed=bas64_hashed_number1, base64_hashed_number2
  ``

  > **Note**  
  > This query parameter is obligatorily  
  > Phone numbers consist of region and number  
  > Phone numbers have to be hashed with a **SHA256** function and encrypted in **Base64**

* Response body:

  ```json
  {
  "list": [
    {
      "id": 9998,
      "phoneNumberHashed": "number_hashed1", 
      "name": "test_user1"
    },
    {
      "id": 9998,
      "phoneNumberHashed": "number_hashed2", 
      "name": "test_user2",
      "bio": "test2"
    }
  ]
  }
  ```

<br>

**DELETE** `- remove user from the service`



<br>

## Me `/api/user/me`

Operations:

**GET** `- Get your information`

- Response Body:

```json
{
  "id": 9998,
  "phoneNumberHashed": "phone_number_hashed",
  "name": "test_user2",
  "bio": "test2"
}
```

<br>

## Chat `/api/user/chat`

Operations:

**GET** - `Returns all messages`

* Return Body:

```json
{
  "list": [
    {
      "id": 123,
      "userFrom": "user_from_hashed_number",
      "chat_to": 1, 
      "content": "base64_encoded_image",
      "template_name": "template_unique_name",
      "created_at": "2040-05-09 00:11:12.908501"
    }
  ]
}
```

<br>


## Chat `/api/user/chat/{id}`
Operations:  

**POST** - `Send a postcard`
* Request Body:
```json
{
  "content": "base64_svg",
  "templateName": "name_of_the_template"
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

