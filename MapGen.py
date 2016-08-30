import sys
import argparse
import re
import random

__author__ = 'Isaac'


def file_name(f):
    try:
        return re.match(r'^[A-Za-z][a-zA-Z0-9_]+', f).group(0)
    except:
        raise argparse.ArgumentTypeError("File name contained invalid characters: %s" % (f,))


def blocked_range(x):
    x = float(x)
    if x > 1.0 or x < 0.0:
        raise argparse.ArgumentTypeError("%r not in range [0.0, 1.0]" % (x,))
    return x

parse = argparse.ArgumentParser()
parse.add_argument("rows", type=int, help="the number of rows in the map")
parse.add_argument("cols", type=int, help="the number of cols in the map")
parse.add_argument("blocked", type=blocked_range, help="the percentage of blocked tiles")
parse.add_argument("fname", type=file_name, help="the name of the output file")
args = parse.parse_args(sys.argv[1:])

print("All targets will be randomly generated")

while True:
    try:
        tNum = int(input("Please enter number of targets: "))
    except ValueError:
        print("Invalid input, must be an integer between {}".format([0, args.rows * args.cols]))
        continue

    if tNum <= args.rows * args.cols:
        break
    else:
        print("Invalid input, must be an integer between {}".format([0, args.rows * args.cols]))

sPos = (random.randint(0, args.rows - 1), random.randint(0, args.cols - 1))
tPos = set()
while len(tPos) < tNum:
    tPos.add((random.randint(0, args.rows - 1), random.randint(0, args.cols - 1)))

args.fname += ".txt"
file = open(args.fname, 'w')
file.write(str(args.rows) + "\n" + str(args.cols) + "\n")

i = 0
while i < args.rows:
    j = 0
    while j < args.cols:
        if (i, j) == sPos:
            file.write("@")
        elif (i, j) in tPos:
            file.write("*")
        else:
            if random.random() <= args.blocked:
                file.write("#")
            else:
                file.write("_")
        j += 1
    file.write("\n")
    i += 1
