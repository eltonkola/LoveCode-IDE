#!/bin/bash
rm -rf api
git clone https://github.com/rm-code/love-api api
lua html-generator.lua
rm ../presentation/src/main/assets/reference.html
mv index.html ../presentation/src/main/assets/reference.html
rm -rf api