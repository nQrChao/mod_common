import shutil
import os
import re

def renameExt(fromExt,to):
	for root,dirs, files in os.walk("."):
		for f in files :
			if(f.endswith(fromExt)):
				a = os.path.splitext(f)
				os.rename(f,a[0]+to)

def rename2(ext,pre,num):
	temp_renamelist = []
	for root,dirs, files in os.walk("."):
		for f in files :
			if(f.endswith(ext)):
				a = os.path.splitext(f)
				parts = re.match(r"("+ pre +")(\d+)",a[0]) 
				if(parts is not None):
					filename = parts.group(1)+ str(int(parts.group(2))+num) + ext
					print(f,filename)
					os.rename(f,"t_"+filename)
					temp_renamelist.append(filename)
	for a in temp_renamelist:
		os.rename("t_"+a , a )




rename2(".json" ,"leva_", 2)