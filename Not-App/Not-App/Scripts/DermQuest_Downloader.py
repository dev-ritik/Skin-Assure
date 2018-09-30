import requests
import os
import urllib.request
from bs4 import BeautifulSoup as Soup
from img_urls import *

maindir = os.getcwd()
os.mkdir('basal')
os.chdir(maindir+'/basal')
i=0
for link in basal:
		page = requests.get(link)
		soup = Soup(page.content,'html.parser')
		fig = soup.find("li",{"data-image-id":os.path.split(link)[1]}).find("figure")
		url = 'https://www.dermquest.com' + fig.find("a")['href']
		urllib.request.urlretrieve(url,'basal'+str(i))
		i=i+1

os.chdir(maindir)
os.mkdir('dermatofibroma')
os.chdir(maindir+'/dermatofibroma')
i=0
for link in derma:
		page = requests.get(link)
		soup = Soup(page.content,'html.parser')
		fig = soup.find("li",{"data-image-id":os.path.split(link)[1]}).find("figure")
		url = 'https://www.dermquest.com' + fig.find("a")['href']
		urllib.request.urlretrieve(url,'derma'+str(i))
		i=i+1
os.chdir(maindir)
os.mkdir('melanoma')
os.chdir(maindir+'/melanoma')
i=0
for link in melanoma:
		page = requests.get(link)
		soup = Soup(page.content,'html.parser')
		fig = soup.find("li",{"data-image-id":os.path.split(link)[1]}).find("figure")
		url = 'https://www.dermquest.com' + fig.find("a")['href']
		urllib.request.urlretrieve(url,'melanoma'+str(i))
		i=i+1
os.chdir(maindir)
os.mkdir('nevus')
os.chdir(maindir+'/nevus')
i=0
for link in nevus:
		page = requests.get(link)
		soup = Soup(page.content,'html.parser')
		fig = soup.find("li",{"data-image-id":os.path.split(link)[1]}).find("figure")
		url = 'https://www.dermquest.com' + fig.find("a")['href']
		urllib.request.urlretrieve(url,'nevus'+str(i))
		i=i+1
os.chdir(maindir)
os.mkdir('pyogenic granuloma')
os.chdir(maindir+'/pyogenic granuloma')
i=0
for link in pyogenic:
		page = requests.get(link)
		soup = Soup(page.content,'html.parser')
		fig = soup.find("li",{"data-image-id":os.path.split(link)[1]}).find("figure")
		url = 'https://www.dermquest.com' + fig.find("a")['href']
		urllib.request.urlretrieve(url,'pyogenic'+str(i))
		i=i+1

os.chdir(maindir)
os.mkdir('seborrheic keratosis')
os.chdir(maindir+'/seborrheic keratosis')
i=0
for link in sebor:
		page = requests.get(link)
		soup = Soup(page.content,'html.parser')
		fig = soup.find("li",{"data-image-id":os.path.split(link)[1]}).find("figure")
		url = 'https://www.dermquest.com' + fig.find("a")['href']
		urllib.request.urlretrieve(url,'sebor'+str(i))		
		i=i+1