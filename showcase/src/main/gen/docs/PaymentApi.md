# PaymentApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getBalance**](PaymentApi.md#getBalance) | **GET** /payment | Получить текущий баланс |
| [**makePayment**](PaymentApi.md#makePayment) | **POST** /payment | Провести платёж |


<a id="getBalance"></a>
# **getBalance**
> BalanceDto getBalance(accountId)

Получить текущий баланс

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    PaymentApi apiInstance = new PaymentApi(defaultClient);
    Long accountId = 56L; // Long | 
    try {
      BalanceDto result = apiInstance.getBalance(accountId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentApi#getBalance");
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
| **accountId** | **Long**|  | |

### Return type

[**BalanceDto**](BalanceDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Баланс успешно получен |  -  |
| **404** | Аккаунт не найден |  -  |

<a id="makePayment"></a>
# **makePayment**
> makePayment(accountId, paymentRequestDto)

Провести платёж

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    PaymentApi apiInstance = new PaymentApi(defaultClient);
    Long accountId = 56L; // Long | 
    PaymentRequestDto paymentRequestDto = new PaymentRequestDto(); // PaymentRequestDto | 
    try {
      apiInstance.makePayment(accountId, paymentRequestDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentApi#makePayment");
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
| **accountId** | **Long**|  | |
| **paymentRequestDto** | [**PaymentRequestDto**](PaymentRequestDto.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Платёж успешно проведён |  -  |
| **400** | Недостаточно средств или некорректные данные |  -  |
| **503** | Сервис недоступен |  -  |

