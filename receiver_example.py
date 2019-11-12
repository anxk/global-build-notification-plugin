import json
import sys

import bottle


@bottle.post("/receiver")
def receiver():
	data = bottle.request.json
	print(json.dumps(data, indent=4))


if __name__ == '__main__':
	if len(sys.argv) != 3:
		print("USAGE: python {} IP PORT".format(__file__))
		sys.exit(1)
	else:
		bottle.run(host=sys.argv[1], port=sys.argv[2], debug=True)
