
love = require('api.love_api')
JSON = (loadfile "JSON.lua")() 


local raw_json_text    = JSON:encode(love)    

function string_to_file(s, f)
    file = io.open(f, 'w')
    file:write(s)
    file:close()
end

string_to_file(raw_json_text, 'docs.json')
