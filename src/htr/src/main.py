from pipeline import Pipeline
import argparse

parser = argparse.ArgumentParser(
    prog = 'PostChat OCR tool',
    description = 'OCR Tool for the PostChat backend'
)

parser.add_argument('-s', '--source', type=str, required=True, nargs=1, help='The path to the source file')
parser.add_argument('-d', '--debug', required=False, action='store_true', help='Show debug options')

if __name__ == '__main__':
    args = parser.parse_args()
    if args.debug:
        print(Pipeline('./../model').recognize(args.source[0], debug=True, verbose=1))
    else:
        print(Pipeline('./../model').recognize(args.source[0], debug=False, verbose=0))