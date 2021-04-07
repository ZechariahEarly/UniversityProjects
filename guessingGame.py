import random


def random_num(a, b):
    num = random.randint(a, b)
    return num


if __name__ == '__main__':
    rand_int = random_num(-100, 100)
    param = 7
    score = 0
    flag = False
    username = input("Enter your Username: ")
    while param > 0:
        guess = int(input("enter guess between -100 to 100: "))
        param = param - 1
        if guess == rand_int:
            flag = True
            break
        elif guess > rand_int:
            print("High guess\n")
        elif guess < rand_int:
            print("Low Guess\n")
    if flag == True:
        print("Great job you guessed it!")
    else:
        print("Sorry, better luck next time")

    with open("scores.txt", "a") as scores:
        scores.write(username+"\t"+str(7-param)+"\n")
