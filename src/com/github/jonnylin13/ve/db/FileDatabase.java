package com.github.jonnylin13.ve.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.VEConfig;
import com.github.jonnylin13.ve.objects.VEGroup;
import com.github.jonnylin13.ve.objects.VEUser;
import com.github.jonnylin13.ve.tools.Filez;
import com.github.jonnylin13.ve.tools.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FileDatabase {
	
	private File groups;
	private File users;
	private File config;
	private VEPlugin vep;
	
	public FileDatabase(VEPlugin instance) {
		this.vep = instance;
		this.groups = new File(getFilePath("groups.json"));
		// this.users = new File(getFilePath("users.json"));
		this.config = new File(getFilePath("config.json"));
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
				file.createNewFile();
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
		// provisionFile(this.users);
		provisionFile(this.config);
	}
	
	public <T> void save(File file, Supplier<T> func) throws IOException {
		try (Writer writer = new FileWriter(file.getAbsolutePath())) {
			Json.toJson(func.get(), writer);
		}
	}
	
	public <T> void saveAsync(File file, Supplier<T> func) throws IOException {
		this.vep.getServer().getScheduler().runTaskLater(this.vep, new SaveAsync<T>(this, file, func), 0L);
	}
	
	public VEConfig loadConfig() throws IOException {
		String jsonString = Filez.readFile(this.config, Charset.defaultCharset());
		Gson gson = new Gson();
		VEConfig result = gson.fromJson(jsonString, VEConfig.class);
		if (result == null) return new VEConfig("", "", "", "");
		return result;
	}
	
	@Deprecated
	public HashMap<UUID, VEUser> loadUsers() throws IOException {
		String jsonString = Filez.readFile(this.users, Charset.defaultCharset());
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<UUID, VEUser>>() {}.getType();
		HashMap<UUID, VEUser> result = gson.fromJson(jsonString, type);
		if (result == null) return new HashMap<UUID, VEUser>();
		return result;
	}
	
	@Deprecated
	public HashMap<UUID, VEUser> loadUsersAsync() throws IOException {
		Future<HashMap<UUID, VEUser>> result = this.vep.getServer().getScheduler().callSyncMethod(this.vep, new LoadUsersAsync(this));
		try {
			// TODO: Needs testing
			VEPlugin.log.info("<VE> Database load users task completed!");
			return result.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new HashMap<UUID, VEUser>();
	}
	
	public HashMap<String, VEGroup> loadGroups() throws IOException {
		String jsonString = Filez.readFile(this.groups, Charset.defaultCharset());
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<String, VEGroup>>() {}.getType();
		HashMap<String, VEGroup> result = gson.fromJson(jsonString, type);
		if (result == null) return new HashMap<String, VEGroup>();
		return result;
	}
	
	public HashMap<String, VEGroup> loadGroupsAsync() throws IOException {
		Future<HashMap<String, VEGroup>> result = this.vep.getServer().getScheduler().callSyncMethod(this.vep, new LoadGroupsAsync(this));
		try {
			// TODO: Needs testing
			VEPlugin.log.info("<VE> Database load groups task completed!");
			return result.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new HashMap<String, VEGroup>();
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
	public class LoadUsersAsync implements Callable<HashMap<UUID, VEUser>> {
		
		private FileDatabase db;
		
		public LoadUsersAsync(FileDatabase db) {
			this.db = db;
		}

		@Override
		public HashMap<UUID, VEUser> call() throws IOException {
			try {
				return this.db.loadUsers();
			} catch(IOException e) {
				e.printStackTrace();
			}
			return new HashMap<UUID, VEUser>();
		}
	}
	
	public class LoadGroupsAsync implements Callable<HashMap<String, VEGroup>> {
		private FileDatabase db;
		
		public LoadGroupsAsync(FileDatabase db) {
			this.db = db;
		}
		
		@Override
		public HashMap<String, VEGroup> call() throws IOException {
			try {
				return this.db.loadGroups();
			} catch(IOException e) {
				e.printStackTrace();
			}
			return new HashMap<String, VEGroup>();
		}
	}


}
