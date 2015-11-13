import math
def findPrimeBelow(z):
    naturalLst = list(range(1,z))
    p = 2
    while p**2 < z:
        naturalLst[p+p-1::p] = [0]*len(naturalLst[p+p-1::p])
        p+= 1

        while (naturalLst[p-1]== 0 ):
            p+= 1
    naturalLst = [item for item in naturalLst if item > 1]
    return naturalLst
def findPrimeDivisor(number,prime_lst):
    st = set()
    while number!= 1:
        for prime in prime_lst:
            while number%prime == 0:
                number = number/prime
                st.add(prime)
    return st
def generate_is_bsmooth(b_smooth,prime_list):
	def is_bsmooth(r):
		max_p = max(findPrimeDivisor(r,prime_list))
		return b_smooth >= max_p
	return is_bsmooth
N = 16637
prime_list = findPrimeBelow(10000)
b_smooth = 30 
factor_base = findPrimeBelow(30) 
L = len(factor_base) + 2
r_arr = []
j = 1
k = 1
jump = 0
is_bsmooth = generate_is_bsmooth(b_smooth,prime_list)
while len(r_arr) < L:
	for k in range(1,11):
		for j in range(1,11):
			ji = j + jump
			ki = k + jump
			r = math.trunc(math.sqrt(ki*N)) + ji 	
			if is_bsmooth(r**2%N):
				print( ki, ji, r )
				r_arr.append(r)				
	jump += 10
print(r_arr)
 
	
