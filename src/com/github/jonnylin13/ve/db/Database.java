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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.VEUser;
import com.github.jonnylin13.ve.tools.Filez;
import com.github.jonnylin13.ve.tools.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Database {
	
	private File groups;
	private File users;
	private VEPlugin vep;
	
	public Database(VEPlugin instance) {
		this.vep = instance;
		this.groups = new File(getFilePath("groups.json"));
		this.users = new File(getFilePath("users.json"));
		this.provision();
	}
	
	// ===========
	// Private API
	// ===========
	
	private String getFilePath(String filename) {
		return (this.vep.getDataFolder().getAbsolutePath() + "/" + filename);
	}
	
	private static void provisionDir(File dir) {
		if (!dir.exists()) dir.mkdir();
	}
	
	private static void provisionFile(File file) {
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
		provisionFile(this.users);
	}
	
	public static <T> void save(File file, Supplier<T> func) throws IOException {
		try (Writer writer = new FileWriter(file.getAbsolutePath())) {
			Json.toJson(func.get(), writer);
		}
	}
	
	public HashMap<UUID, VEUser> loadUsers() throws IOException {
		String jsonString;
		jsonString = Filez.readFile(this.users, Charset.defaultCharset());
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<UUID, VEUser>>() {}.getType();
		return gson.fromJson(jsonString, type);
	}
	
	public <T> void saveAsync(File file, Supplier<T> func) throws IOException {
		Executors.newSingleThreadExecutor().execute(new SaveAsync<T>(file, func));
	}
	
	public HashMap<UUID, VEUser> loadUsersAsync() throws IOException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<HashMap<UUID, VEUser>> result = executor.submit(new LoadUsersAsync(this));
		try {
			// TODO: Needs testing
			VEPlugin.log.info("<VE> Database load task completed!");
			return result.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
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
		private File file;
		private Supplier<T> func;
		
		public SaveAsync(File file, Supplier<T> func) {
			this.file = file;
			this.func = func;
		}
		
		@Override
		public void run() {
			try {
				Database.save(this.file, this.func);
				// TODO: Needs testing
				VEPlugin.log.info("<VE> Database save task complete!");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class LoadUsersAsync implements Callable<HashMap<UUID, VEUser>> {
		
		private Database db;
		
		public LoadUsersAsync(Database db) {
			this.db = db;
		}

		@Override
		public HashMap<UUID, VEUser> call() throws Exception {
			try {
				return this.db.loadUsers();
			} catch(IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}


}
