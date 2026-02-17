# DefaultApi

All URIs are relative to *http://localhost:8081/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**authLoginPost**](DefaultApi.md#authLoginPost) | **POST** /auth/login | Аутентификация пользователя |
| [**authMeGet**](DefaultApi.md#authMeGet) | **GET** /auth/me | Получение данных текущего пользователя |
| [**authRefreshTokenPost**](DefaultApi.md#authRefreshTokenPost) | **POST** /auth/refresh-token | Обновление токена доступа |
| [**authRegistrationPost**](DefaultApi.md#authRegistrationPost) | **POST** /auth/registration | Регистрация пользователя |


<a id="authLoginPost"></a>
# **authLoginPost**
> TokenResponse authLoginPost(userLoginRequest)

Аутентификация пользователя

### Example
```java
// Import classes:
import com.tmq.individuals_api.ApiClient;
import com.tmq.individuals_api.ApiException;
import com.tmq.individuals_api.Configuration;
import com.tmq.individuals_api.models.*;
import com.tmq.individuals_api.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8081/v1");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    UserLoginRequest userLoginRequest = new UserLoginRequest(); // UserLoginRequest | 
    try {
      TokenResponse result = apiInstance.authLoginPost(userLoginRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#authLoginPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **userLoginRequest** | [**UserLoginRequest**](UserLoginRequest.md)|  | |

### Return type

[**TokenResponse**](TokenResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Успешная аутентификация |  -  |
| **401** | Неверный логин или пароль |  -  |

<a id="authMeGet"></a>
# **authMeGet**
> UserInfoResponse authMeGet()

Получение данных текущего пользователя

### Example
```java
// Import classes:
import com.tmq.individuals_api.ApiClient;
import com.tmq.individuals_api.ApiException;
import com.tmq.individuals_api.Configuration;
import com.tmq.individuals_api.auth.*;
import com.tmq.individuals_api.models.*;
import com.tmq.individuals_api.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8081/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    try {
      UserInfoResponse result = apiInstance.authMeGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#authMeGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**UserInfoResponse**](UserInfoResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Данные пользователя |  -  |
| **401** | Недействительный токен |  -  |
| **404** | Пользователь не найден |  -  |

<a id="authRefreshTokenPost"></a>
# **authRefreshTokenPost**
> TokenResponse authRefreshTokenPost(tokenRefreshRequest)

Обновление токена доступа

### Example
```java
// Import classes:
import com.tmq.individuals_api.ApiClient;
import com.tmq.individuals_api.ApiException;
import com.tmq.individuals_api.Configuration;
import com.tmq.individuals_api.models.*;
import com.tmq.individuals_api.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8081/v1");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(); // TokenRefreshRequest | 
    try {
      TokenResponse result = apiInstance.authRefreshTokenPost(tokenRefreshRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#authRefreshTokenPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **tokenRefreshRequest** | [**TokenRefreshRequest**](TokenRefreshRequest.md)|  | |

### Return type

[**TokenResponse**](TokenResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Токен успешно обновлён |  -  |
| **401** | Недействительный или просроченный refresh token |  -  |

<a id="authRegistrationPost"></a>
# **authRegistrationPost**
> TokenResponse authRegistrationPost(userRegistrationRequest)

Регистрация пользователя

### Example
```java
// Import classes:
import com.tmq.individuals_api.ApiClient;
import com.tmq.individuals_api.ApiException;
import com.tmq.individuals_api.Configuration;
import com.tmq.individuals_api.models.*;
import com.tmq.individuals_api.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8081/v1");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(); // UserRegistrationRequest | 
    try {
      TokenResponse result = apiInstance.authRegistrationPost(userRegistrationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#authRegistrationPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **userRegistrationRequest** | [**UserRegistrationRequest**](UserRegistrationRequest.md)|  | |

### Return type

[**TokenResponse**](TokenResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Успешная регистрация |  -  |
| **400** | Ошибка валидации |  -  |
| **409** | Пользователь уже существует |  -  |

