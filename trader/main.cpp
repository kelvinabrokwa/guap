#include <iostream>
#include "curl_easy.h"
#include "curl_exception.h"
#include <json/json.h>

using curl::curl_easy;
using curl::curl_easy_exception;
using curl::curlcpp_traceback;

int main() {
    curl_easy easy;
    easy.add<CURLOPT_URL>("http://www.google.com");
    try {
        easy.perform();
    } catch (curl_easy_exception error) {
        curlcpp_traceback errors = error.get_traceback();
        error.print_traceback();
    }
    return 0;
}