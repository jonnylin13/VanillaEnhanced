package com.github.jonnylin13.ve.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.Config;
import com.github.jonnylin13.ve.objects.Group;
import com.github.jonnylin13.ve.objects.User;
import com.github.jonnylin13.ve.tools.Filez;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileDatabase {
	
	public static String GROUPS = "groups.json";
	public static String CONFIG = "config.json";
	
	private File groups;
	private File users;
	private File config;
	private VEPlugin vep;
	
	public FileDatabase(VEPlugin instance) {
		this.vep = instance;
		this.groups = new File(getFilePath(GROUPS));
		this.config = new File(getFilePath(CONFIG));
		this.provision();
	}
	
	// ===========
	// Private API
	// ===========
	
	private String getFilePath(String filename) {
		return (this.vep.getDataFolder().getAbsolutePath() + "/" + filename);
	}
	
	private void provisionDir(File dir) {
		if (!dir.exists()) dir.mkdir();
	}
	
	private void provisionFile(File file) {
		if (!file.exists()) {
			try {
				InputStream is = this.getClass().getClassLoader().getResourceAsStream(file.getName());
				OutputStream out = new FileOutputStream(file);
				try {
					byte[] buffer = new byte[is.available()];
					for (int i = 0; i != -1; i = is.read(buffer)) {
						out.write(buffer, 0, i);
					}
				} finally {
					is.close();
					out.close();
				}
				Files.copy(new File(Paths.get("src", file.getName()).toFile().getAbsolutePath()), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ==========
	// Public API
	// ==========
	
	public void provision() {
		provisionDir(this.vep.getDataFolder());
		provisionFile(this.groups);
		provisionFile(this.config);
	}
	
	public <T> void save(File file, Supplier<T> func) throws IOException {
		try (Writer writer = new FileWriter(file.getAbsolutePath())) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(func.get(), writer);
		}
	}
	
	public <T> void saveAsync(File file, Supplier<T> func) throws IOException {
		this.vep.getServer().getScheduler().runTaskLater(this.vep, new SaveAsync<T>(this, file, func), 0L);
	}
	
	public Config loadConfig() throws IOException {
		String jsonString = Filez.readFile(this.config, Charset.defaultCharset());
		Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
		Config result = gson.fromJson(jsonString, Config.class);
		if (result == null) return new Config("", "", "", "");
		return result;
	}
	
	@Deprecated
	public HashMap<UUID, User> loadUsers() throws IOException {
		String jsonString = Filez.readFile(this.users, Charset.defaultCharset());
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<UUID, User>>() {}.getType();
		HashMap<UUID, User> result = gson.fromJson(jsonString, type);
		if (result == null) return new HashMap<UUID, User>();
		return result;
	}
	
	@Deprecated
	public HashMap<UUID, User> loadUsersAsync() throws IOException {
		Future<HashMap<UUID, User>> result = this.vep.getServer().getScheduler().callSyncMethod(this.vep, new LoadUsersAsync(this));
		try {
			// TODO: Needs testing
			VEPlugin.log.info("<VE> Database load users task completed!");
			return result.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new HashMap<UUID, User>();
	}
	
	public HashMap<String, Group> loadGroups() throws IOException {
		String jsonString = Filez.readFile(this.groups, Charset.defaultCharset());
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<String, Group>>() {}.getType();
		HashMap<String, Group> result = gson.fromJson(jsonString, type);
		if (result == null) return new HashMap<String, Group>();
		else {
			for (String name : result.keySet()) 
				result.get(name).setName(name);
		}
		return result;
	}
	
	public HashMap<String, Group> loadGroupsAsync() throws IOException {
		Future<HashMap<String, Group>> result = this.vep.getServer().getScheduler().callSyncMethod(this.vep, new LoadGroupsAsync(this));
		try {
			// TODO: Needs testing
			VEPlugin.log.info("<VE> Database load groups task completed!");
			return result.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new HashMap<String, Group>();
	}
	
	public File getUsersFile() {
		return this.users;
	}
	
	public File getGroupsFile() {
		return this.groups;
	}
	
	// ===========
	// Async Tasks
	// ===========
	
	public class SaveAsync<T> implements Runnable {
		
		private FileDatabase db;
		private File file;
		private Supplier<T> func;
		
		public SaveAsync(FileDatabase db, File file, Supplier<T> func) {
			this.file = file;
			this.func = func;
		}
		
		@Override
		public void run() {
			try {
				// TODO: Null pointer exception 12/5
				this.db.save(this.file, this.func);
				// TODO: Needs testing
				VEPlugin.log.info("<VE> Database save task complete!");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Deprecated
	public class LoadUsersAsync implements Callable<HashMap<UUID, User>> {
		
		private FileDatabase db;
		
		public LoadUsersAsync(FileDatabase db) {
			this.db = db;
		}

		@Override
		public HashMap<UUID, User> call() throws IOException {
			try {
				return this.db.loadUsers();
			} catch(IOException e) {
				e.printStackTrace();
			}
			return new HashMap<UUID, User>();
		}
	}
	
	public class LoadGroupsAsync implements Callable<HashMap<String, Group>> {
		private FileDatabase db;
		
		public LoadGroupsAsync(FileDatabase db) {
			this.db = db;
		}
		
		@Override
		public HashMap<String, Group> call() throws IOException {
			try {
				return this.db.loadGroups();
			} catch(IOException e) {
				e.printStackTrace();
			}
			return new HashMap<String, Group>();
		}
	}


}
