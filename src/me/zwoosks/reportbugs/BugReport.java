package me.zwoosks.reportbugs;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BugReport extends JavaPlugin {
	
	public Permission playerPermission = new Permission("bugsserver");

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.addPermission(playerPermission);
	}
	
	
	@Override
	public void onDisable() {
		
	}
	
	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		FileConfiguration config = this.getConfig();
		
		Player player = (Player) sender;
		
		
		// Reportar un bug
		if(cmd.getName().equalsIgnoreCase("bug") && sender instanceof Player) {
			
			if(player.hasPermission("bugsserver.reportar")) {
				
				if(args.length >= 1) {
					
					String bugMessage = "";
					
					for(String arg : args) {
						bugMessage = bugMessage + arg + " ";
					}
					
					// Handling arguments
					if(!config.contains(player.getName().toLowerCase())) {
						
						config.set(player.getName().toLowerCase(), bugMessage);
						player.sendMessage(ChatColor.GREEN + "Has reportado el bug de forma exitosa.");
						saveConfig();
						
					} else {
						player.sendMessage(ChatColor.RED + "Solamente puedes tener hasta un reporte activo en el sistema de bugs.");
					}
					
				} else {
					player.sendMessage(ChatColor.RED + "Inserta tu reporte despues del comando /bug");
				}
				
				return true;
				
				
			}
			
		}
		
		
		// Ver todos los bugs
		if(cmd.getName().equalsIgnoreCase("verbugs") && sender instanceof Player) {
			if(player.hasPermission("bugsserver.verbugs")) {
				
				for(String clave : config.getKeys(false)) {
					player.sendMessage(ChatColor.YELLOW + clave + ": " + config.getString(clave));
				}
				
				if(config.getKeys(false).isEmpty()) {
					player.sendMessage(ChatColor.GREEN + "No hay bugs para revisar.");
				}
				
				
			} else {
				player.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
			}
			
			return true;
			
		}
		
		
		
		// Eliminar bugs ya reparados
		if(cmd.getName().equalsIgnoreCase("eliminarbug") && sender instanceof Player) {
			if(player.hasPermission("bugsserver.eliminarbugs")) {
				
				if(args.length == 1) {
					if(config.contains(args[0].toLowerCase())) {
						
						config.set(args[0], null);
						saveConfig();
						player.sendMessage(ChatColor.GREEN + "Bug eliminado de la lista exitosamente.");
						
					} else {
						player.sendMessage(ChatColor.RED + "Esta persona no ha reportado ningun bug.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Argumentos incorrectos.");
				}
				
			} else {
				player.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
			}
			
			
			return true;
		}
		
		
		return false;
		
	}
	
}
