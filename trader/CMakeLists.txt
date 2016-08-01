cmake_minimum_required(VERSION 3.5)
project(trader)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

ADD_SUBDIRECTORY(lib/curlcpp) # Change `ext/curlcpp` to a directory according to your setup
INCLUDE_DIRECTORIES(${CURLCPP_SOURCE_DIR}/include)

set(SOURCE_FILES main.cpp)
add_executable(trader ${SOURCE_FILES})

target_link_libraries(trader curlcpp)

