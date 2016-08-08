cmake_minimum_required(VERSION 3.5)
project(trader)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

# curlcpp
ADD_SUBDIRECTORY(lib/curlcpp)
INCLUDE_DIRECTORIES(${CURLCPP_SOURCE_DIR}/include)

# jsoncpp
ADD_SUBDIRECTORY(lib/jsoncpp)
INCLUDE_DIRECTORIES(${jsoncpp_SOURCE_DIR}/include)

set(SOURCE_FILES main.cpp)
add_executable(trader ${SOURCE_FILES})

target_link_libraries(trader curlcpp ${jsoncpp_lib})

