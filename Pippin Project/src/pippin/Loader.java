package pippin;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Loader {
	static public void load(Memory m, File f) throws IOException {
		Scanner file = new Scanner(f);
		int index = 0;
		boolean inData = false;
		while (file.hasNextLine()) {
			String line = file.nextLine();

			if (line.equals("11111111111111111111111111111111")) {
				inData = true;
				line = file.nextLine();
			}

			if (!inData) {
				long arg = 0;
				long opcode = Long.parseLong(line, 2);
				// boolean oc = Assembler.opcode.containsValue(opcode);
				String o = null;
				// if(oc) {

				o = Assembler.first(Assembler.opcode, (int) opcode / 4);
				// }
				System.out.print("Opcode: " + opcode + " Instruction : " + o
						+ "    ");
				if (!Assembler.noArgument.contains(o)) {
					String dline = file.nextLine();
					if (!(dline.charAt(0) == '1' && dline.length() == 32)) { // if
																				// not
																				// negative
						arg = Long.parseLong(dline, 2);
					} else {
						StringBuilder str = new StringBuilder();
						for (int i = 0; i < dline.length(); i++) {
							if (dline.charAt(i) == '0')
								str.append('1');
							else
								str.append('0');
						}
						arg = Long.parseLong(str.toString(), 2) + 1; // make it
																		// properly
																		// negative
						arg *= -1;
					}
				}
				try {
					System.out.println("Index: " + index + " Opcode:  "
							+ opcode + " Arg: " + arg + "\n");
					m.setCode(index, (int) opcode, (int) arg);
				} catch (CodeAccessException e) {
					System.out.println("ERROR: Cannot access code location "
							+ index);
				}
			} else {

				int address = Integer.parseInt(line, 2);
				if (file.hasNextInt()) {
					System.out.println(address);
					int value = Integer.parseInt(file.nextLine(), 2);
					try {
						m.setData(address, value);
					} catch (DataAccessException e) {
						System.out
								.println("ERROR: Cannot access data location "
										+ address);
					}
				}

			}
			index++;
		}
		file.close();

	}
}
