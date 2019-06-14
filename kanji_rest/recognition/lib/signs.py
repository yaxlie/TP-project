import io
import json
import csv

def get_signs():
	f = open( './k49_classmap.csv', 'rU', encoding='utf-8' )  
	reader = csv.DictReader( f, fieldnames = ( "index","codepoint","char","rom" ))  
	next(reader, None)
	out = json.dumps( [ row for row in reader ] ) 

	return out