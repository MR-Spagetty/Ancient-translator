#
# ancient_translator.py

from sys import argv

CHARS = {
    'a': [
        "███",
        "  █",
        "███",
        "  █"
    ],
    'b': [
        "███",
        " ██",
        "  █",
        "███"
    ],
    'c': [
        "███",
        "█ █",
        "█ █",
        "█ █"
    ],
    'd': [
        "███",
        " █ ",
        "███",
        "███"
    ],
    'e': [
        "█ █",
        "█  ",
        "███",
        "█ █"
    ],
    'f': [
        "███",
        "  █",
        "  █",
        "  █"
    ],
    'g': [
        "█ █",
        "█ █",
        "█  ",
        "███"
    ],
    'h': [
        "███",
        "  █",
        "███",
        "█ █"
    ],
    'i': [
        "███",
        "   ",
        " █ ",
        "███"
    ],
    'j': [
        "███",
        "█ █",
        "   ",
        "███"
    ],
    'k': [
        "██ ",
        "██ ",
        " █ ",
        "██ "
    ],
    'l': [
        " ██",
        "██ ",
        " ██",
        "██ "
    ],
    'm': [
        "█ █",
        "█ █",
        " █ ",
        "███"
    ],
    'n': [
        " █ ",
        " ██",
        "███",
        "█ █"
    ],
    'o': [
        "█ █",
        "██ ",
        " ██",
        "█ █"
    ],
    'p': [
        "█ █",
        "█  ",
        "█  ",
        "█ █"
    ],
    'q': [
        " █ ",
        "███",
        " █ ",
        "█ █"
    ],
    'r': [
        "███",
        "   ",
        "█ █",
        "███"
    ],
    's': [
        "█ █",
        "███",
        "█ █",
        "█  "
    ],
    't': [
        "█ █",
        "   ",
        "███",
        "█ █"
    ],
    'u': [
        "███",
        "  █",
        "  █",
        "  █"
    ],
    'v': [
        "███",
        " ██",
        " ██",
        "███"
    ],
    'w': [
        "█ █",
        "███",
        " ██",
        "█ █"
    ],
    'x': [
        "█ █",
        "█ █",
        "███",
        "█ █"
    ],
    'y': [
        "███",
        "█ █",
        "█  ",
        "███"
    ],
    'z': [
        "███",
        " █ ",
        "██ ",
        "█ █"
    ],
    '0': [
        "███",
        "█ █",
        "█ █",
        "▀█▀"
    ],
    '1': [
        "█  ",
        "   ",
        "   ",
        "▀█▀"
    ],
    '2': [
        "██ ",
        "   ",
        "   ",
        "▀█▀"
    ],
    '3': [
        "███",
        "   ",
        "   ",
        "▀█▀"
    ],
    '4': [
        "███",
        "█  ",
        "   ",
        "▀█▀"
    ],
    '5': [
        "███",
        "██ ",
        "   ",
        "▀█▀"
    ],
    '6': [
        "███",
        "███",
        "   ",
        "▀█▀"
    ],
    '7': [
        "███",
        "███",
        "█  ",
        "▀█▀"
    ],
    '8': [
        "███",
        "███",
        "██ ",
        "▀█▀"
    ],
    '9': [
        "███",
        "███",
        "███",
        "▀█▀"
    ],
    ' ': [
        "   ",
        "   ",
        "   ",
        "   "
    ],
    '.': [
        "   ",
        "   ",
        "   ",
        "█  "
    ],
    ':': [
        " ▄ ",
        " ▀ ",
        " ▄ ",
        " ▀ "
    ],
    '!': [
        " █ ",
        " █ ",
        "   ",
        " █ "
    ]
}
chars_reversed = {'\n'.join(v): k for k, v in CHARS.items()}


def from_plain_text(english_text: str, letter_spacing: int = 2) -> str:
    print(" ".join(list(english_text.replace('\\', '\n'))))
    output = []
    current_line = ['',
                    '',
                    '',
                    '']
    for char in english_text.lower():
        if char == '\\':
            output.append(
                ("%s\n" % '\n'.join(current_line)) if len(current_line) else
                '\n\n\n\n')
            current_line = ['',
                            '',
                            '',
                            '']
        elif char in CHARS:
            if len(current_line[0]):
                current_line[0] += f'{" "*letter_spacing}{CHARS[char][0]}'
                current_line[1] += f'{" "*letter_spacing}{CHARS[char][1]}'
                current_line[2] += f'{" "*letter_spacing}{CHARS[char][2]}'
                current_line[3] += f'{" "*letter_spacing}{CHARS[char][3]}'
            else:
                current_line[0] += f'{CHARS[char][0]}'
                current_line[1] += f'{CHARS[char][1]}'
                current_line[2] += f'{CHARS[char][2]}'
                current_line[3] += f'{CHARS[char][3]}'
    output.append('\n'.join(current_line))
    return "\n".join(output) if len(output) > 1 else output[0]


def to_plain_text(anceint_text: str, letter_spacing: int = 2) -> str:
    output = []
    working_data = anceint_text.replace('\n\n\n\n', '\n\n')
    print(working_data)
    lines = working_data.split('\n\n')
    for line in lines:
        letters = {0: ''}
        sublines = line.split('\n')
        longest_line = ''
        for subline in sublines:
            if len(subline) > len(longest_line):
                longest_line = subline
        for i in range(len(sublines)):
            if len(sublines[i]) < len(longest_line):
                while len(sublines[i]) < len(longest_line):
                    sublines[i] += ' '
        for subline in sublines:
            letters_done = 0
            char_char_count = 0
            spacing_char_count = 0
            counting_spacing = False
            for char in subline:
                if counting_spacing:
                    spacing_char_count += 1
                    if spacing_char_count == letter_spacing:
                        letters[letters_done] += '\n'
                        letters_done += 1
                        if letters_done not in letters.keys():
                            letters[letters_done] = ''
                        spacing_char_count = 0
                        counting_spacing = False
                else:
                    char_char_count += 1
                    letters[letters_done] += char
                    if char_char_count == 3:
                        char_char_count = 0
                        counting_spacing = True
            letters[letters_done] += '\n'
        for i, char in letters.items():
            try:
                output.append(chars_reversed[char[:-1]])
            except KeyError:
                print(i, f'\n{char}')
        output.append('\n')
    return ''.join(output)


if __name__ == "__main__":
    if len(argv) > 2 and argv[1] == '-r':
        out = to_plain_text(argv[2])
    else:
        out = from_plain_text(argv[1] if len(argv) > 1 else input())
    if '-f' in argv:
        # currently incapable of saving to files
        try:
            with open(
                argv[argv.index('-f')+1] if len(argv) > argv.index('-f') + 1
                    else 'output.txt', 'w') as file:
                file.write(out)
        except UnicodeEncodeError:
            print(out)
    else:
        print(out)
else:
    print(__name__)
