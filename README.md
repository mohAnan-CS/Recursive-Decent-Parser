# Recursive Descent Parser

This project is a recursive descent parser that includes a scanner for tokenizing input and a parser for determining if the input can be successfully parsed. If parsing fails, it displays the line number of the error and an error message with the token number.

## What is a Recursive Descent Parser?

A recursive descent parser is a top-down parsing technique commonly used in programming language compilers to analyze and understand the structure of source code. It starts with the highest-level production rule and recursively breaks down the code into smaller components based on the grammar rules.

In a recursive descent parser, each non-terminal in the grammar is associated with a procedure or function. These procedures are responsible for recognizing the corresponding non-terminal and its components.

## Production Rules

The following are the production rules used in this parser:

| Production Rule                        | Description                                      |
|----------------------------------------|--------------------------------------------------|
| project-declaration → project-def "."   | Description of the project declaration rule      |
| project-def → project-heading ...      | Description of the project definition rule       |
| ...                                    | ...                                              |

| project-declaration → project-def "."
| project-def → project-heading declarations compound-stmt
| project-heading → project "name" ";"
| declarations → const-decl var-decl subroutine-decl
| const-decl → const (const-item ";")+ | ε
| const-item → "name" = "integer-value"
| var-decl → var (var-item ";")+ | ε
| var-item → name-list ":" int
| name-list → "name" ("," "name")*
| subroutine-decl → subroutine-heading declarations compound-stmt ";" | ε
| subroutine-heading → routine "name" ";"
| compund-stmt → start stmt-list end
| stmt-list → (statement ";")*
| statement → ass-stmt | inout-stmt | if-stmt | loop-stmt | ε
| ass-stmt → "name" ":=" arith-exp
| arith-exp → term (add-sign term)*
| term → factor (mul-sign factor)*
| factor → "(" arith-exp ")" | name-value
| name-value → "name" | "integer-value"
| add-sign → "+" | "-"
| mul-sign → "*" | "/" | "%"
| inout-stmt → input "(" "name" ")" | output "(" name-value ")"
| if-stmt → if "(" bool-exp ")" then statement else-part endif 
| else-part → else statement | ε 
| loop-stmt → loop "(" bool-exp ")" do statement 
| bool-exp → name-value relational-oper name-value 
| relational-oper → "=" | "<>" | "<" | "<=" | ">" | ">=" 


To copy the production rules, users can simply select the content of the table and copy it.

Feel free to explore the code and modify it according to your needs.

## Usage

To use this project, follow these steps:

1. Clone the repository to your local machine.
2. Make sure you have Python installed.
3. Open the project in your preferred code editor.
4. Run the `main.py` file.
5. Input the code or text you want to parse.
6. The program will display whether the input was parsed successfully or if there were any errors. If there are errors, it will show the line number and error message along with the token number.

Feel free to explore the code and modify it according to your needs.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## Contact

For any inquiries or questions, you can reach me at [your-email@example.com](mailto:your-email@example.com).

---

Thank you for checking out this recursive descent parser project! I hope you find it useful. Happy parsing!
