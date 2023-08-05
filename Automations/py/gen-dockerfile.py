import argparse


def process_file(input_file, output_file, template_args):
    with open(input_file, 'r') as input_file, open(output_file, 'w') as output_file:
        for line in input_file:
            processed_line = line
            for arg_key, arg_value in template_args.items():
                placeholder = f'<{arg_key}>'
                if placeholder in processed_line:
                    processed_line = processed_line.replace(placeholder, arg_value)
            output_file.write(processed_line)


def main():
    parser = argparse.ArgumentParser(description='Template filler.')
    parser.add_argument('input_file', help='Path to the template.')
    parser.add_argument('--output', '-o', help='Path to the output file.')
    parser.add_argument('--args', help='Arguments for the template. '
                                       '\nEach argument in the template like <MODULE>'
                                       ' should be present in the args like \n"MODULE=my-module" separated with ,')

    args = parser.parse_args()

    # Parse the provided arguments in --args and create a dictionary
    template_args = {}
    if args.args:
        for arg in args.args.split(','):
            key, value = arg.split('=')
            template_args[key.strip()] = value.strip()

    # Process the template and write to the output file
    process_file(args.input_file, args.output, template_args)


if __name__ == '__main__':
    main()
