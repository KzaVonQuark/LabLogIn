package lablog.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Linus Lundahl
 * Enum that handles reading and writing from/to file.
 * Devides and sorts data for storing in textfiles. 
 */
public enum FileManager {
	
	FM;

	private String pathName = "src/files/";
	
	/**
	 * Reads called file, splitts data and store it in array.
	 * @param fileName - name of file to read from. File type not defined.
	 * @return -  arrays with user data.
	 */
	ArrayList<String[]> read(String fileName) { 
		ArrayList<String[]> data = new ArrayList<String[]>();
		try (BufferedReader br = new BufferedReader(new FileReader(pathName + fileName + ".txt"))) {
			String line;
			
			while ((line = br.readLine()) != null) {
				data.add(line.split("[|]"));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return data;
	}
	
	/**
	 * Write user data in associated .txt file (researcher or administrator).
	 * @param user
	 * @param fileName - name on text file to write to.
	 */
	void saveUser(User user, String fileName) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathName + fileName + ".txt", true))) {
			
			bw.append(user.getName() +"|"+ user.getLastName() +"|"+ user.getUserData() + "\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write virus data to file virus.txt
	 * @param lab
	 * @param fileName - name on text file to write to.
	 */
	void saveVirus(Virus virus) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathName + "virus.txt", true))) {
			
			bw.append(virus.getName() +"|"+ virus.getType() +"|"+ virus.getCfr() +"|"+ virus.isEnveloped() + "\n");				
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write bacteria data to file bacteria.txt
	 * @param lab
	 * @param fileName - name on text file to write to.
	 */
	void saveBacteria(Bacteria bacteria) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathName + "bacteria.txt", true))) {
		
			bw.append(bacteria.getName() +"|"+ bacteria.getType() +"|"+ bacteria.getCfr() +"|"+ bacteria.isSpores() + "\n");
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write laboratory data to laboratory.txt
	 * @param lab
	 * @param fileName - name on text file to write to.
	 */
	void saveLaboratory(Laboratory lab) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathName + "laboratory.txt", true))) {
			
			bw.append(lab.getName() +"|"+ lab.getSecurityLevel() + "\n");
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write data from information text area to infotext.txt.
	 * @param info
	 */
	void saveInfotext(String info) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathName + "infotext.txt"))) {
			
			bw.write(info);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void saveLoggedIn(String loggedIn) {
		File loginfile = new File(pathName + "loggedin.txt");
		loginfile.deleteOnExit();
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(loginfile))) {
			
			bw.write(loggedIn);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clear existing file by saving an empty file over the existing file.
	 * @param fileName
	 */
	public void clearFile(String fileName) {
		try (FileWriter fw = new FileWriter(pathName + fileName + ".txt")) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write statistics and cost to file. File is sent to Filewriter from
	 * Filechooser in controller.
	 * @param cost
	 * @param entries
	 * @param file
	 */
	public void writeStatistics(String cost, String entries, File file) { 
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
			bw.write("Cost over selected period\n\n" + cost + "\n\n");
			bw.append("Entries over selected period\n\n" + entries);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // Skriver med relativ sökväg. File är satt av avnändaren (kommer från FileChooser i controllern). 
	
	
	/**
	 * For writing researcher data to logg file when logging out. 
	 */
	void writeLogOut(Researcher researcher) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathName + "logg.txt", true))) {
			bw.append(researcher.getName() +"|"+ researcher.getLastName() +"|"+ researcher.getUserData() +"|"+ researcher.getPathogen() +"|"+
					researcher.getLab() +"|"+ df.format(researcher.getTimeLogIn()) +"|"+ df.format(researcher.getTimeLogOut()) + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
