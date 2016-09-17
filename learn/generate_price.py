#!/usr/bin/env python

import random

def model(r):
    return r

def generate():
    period = 2**19937 - 1
    random.seed()
    while period > 0:
        period -= 1
        r = random.random()
        yield(model(r))

if __name__ == '__main__':
    n = generate()
    for i in range(13):
        print(n.next())
