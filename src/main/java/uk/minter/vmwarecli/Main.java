package uk.minter.vmwarecli;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	private static Options options;
	
	@SuppressWarnings("static-access")
	private static void buildOptions() {
		options = new Options();

		options.addOption(OptionBuilder
				.withLongOpt("hostname")
				.withArgName("hostname")
				.hasArg()
				.isRequired()
				.create("h"));

		options.addOption(OptionBuilder
				.withLongOpt("username")
				.withArgName("username")
				.hasArg()
				.isRequired()
				.create("u"));

		options.addOption(OptionBuilder
				.withLongOpt("password")
				.withArgName("password")
				.hasArg()
				.isRequired()
				.create("p"));

		options.addOption(OptionBuilder
				.withLongOpt("insecure")
				.create("k"));
}
	
	private static void printUsage(final String error) {
		PrintWriter pw = new PrintWriter(System.err);
		pw.println("Usage: vmwarecli [options] cloneVMtoTemplate datacenter/vm/vm-name template-name");
		pw.println("     | vmwarecli [options] deleteVM datacenter/vm/vm-name");
		pw.println();
		pw.println("Options:");
		new HelpFormatter().printOptions(pw, 80, options, 2, 0);
		pw.println();
		pw.println("See https://github.com/jim-minter/vmwarecli for more details.");
		pw.println();
		pw.println(error);
		pw.flush();
	}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, InterruptedException {
		buildOptions(); 

		CommandLine cl;
		try {
			cl = new BasicParser().parse(options, args);
			args = cl.getArgs();
			if(args.length == 0)
				throw new ParseException("Missing required command");
			
		} catch (ParseException e) {
			printUsage(e.getMessage());
			return;
		}

		VMAPI vmapi = new VMAPI(new URL("https://" + cl.getOptionValue("h") + "/sdk"), cl.getOptionValue("u"), cl.getOptionValue("p"), cl.hasOption("k"));

		if(args[0].equalsIgnoreCase("cloneVMtoTemplate") && args.length == 3)
			System.out.println(vmapi.cloneVMtoTemplate(args[1], args[2]));

		else if(args[0].equalsIgnoreCase("deleteVM") && args.length == 2)
			System.out.println(vmapi.deleteVM(args[1]));

		else				
			printUsage("Unrecognised or mis-formed command");
	}
}