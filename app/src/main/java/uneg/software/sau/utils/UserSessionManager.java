package uneg.software.sau.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import uneg.software.sau.R;
import uneg.software.sau.models.BaseResponse;


public class UserSessionManager
{
    private static final String KEY_ID = "id_user";
    private static final String KEY_USERNAME = "userNAME";
    private static final String KEY_CORREO = "correo";
    private static final String KEY_NOMBRE = "NOMBRE";
    private static final String KEY_APELLIDO = "aPPELLIDO";
    private static final String KEY_TELEFONO = "TELEFONO";
    private static final String KEY_AREA = "area";
    private static final String KEY_NOMBRECOMPLETO = "NOMBRE COMPLETO";
    private static final String KEY_NIVEL = "nivel";
    private static final String KEY_ITEM = "item";
    private static final String KEY_NIVEL_DES = "nivel_des";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "SesionPref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // Constructor
    public UserSessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Clear session details
     * */
    public void logoutUser()
    {
        editor.clear();
        editor.apply();
        editor.commit();
    }

    // Check for login
    public boolean isUserLoggedIn()
    {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void setUserLoggedIn(boolean remember)
    {
        editor.putBoolean(IS_USER_LOGIN, remember);
        editor.apply();
        editor.commit();
    }


    public String getIdUser() {
        return pref.getString(KEY_ID, "");
    }

    public void setIdUser(String id)
    {
        editor.putString(KEY_ID, id);
        editor.apply();
        editor.commit();
    }

    public String getUsername()
    {
        return pref.getString(KEY_USERNAME, "");
    }


    public void setUsername(String username)
    {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
        editor.commit();
    }

    public void setCorreo(String correo) {
        editor.putString(KEY_CORREO, correo);
        editor.apply();
        editor.commit();
    }

    public String getCorreo() {
        return pref.getString(KEY_CORREO, "");
    }


    public void setNombre(String nombre) {
        editor.putString(KEY_NOMBRE, nombre);
        editor.apply();
        editor.commit();
    }

    public void setApellido(String apellido) {
        editor.putString(KEY_APELLIDO, apellido);
        editor.apply();
        editor.commit();
    }

    public void setTelefono(String telefono) {
        editor.putString(KEY_TELEFONO, telefono);
        editor.apply();
        editor.commit();
    }


    public void setLastArea(int area) {
        editor.putInt(KEY_AREA, area);
        editor.apply();
        editor.commit();
    }

    public void setNombreCompleto(String value) {
        editor.putString(KEY_NOMBRECOMPLETO, value);
        editor.apply();
        editor.commit();
    }

    public int getLastArea() {
        return pref.getInt(KEY_AREA, Constants.AREA_TODAS);
    }

    public String getNombre() {
        return pref.getString(KEY_NOMBRE, "");
    }

    public String getApellido() {
        return pref.getString(KEY_APELLIDO, "");
    }

    public String getNombreCompleto() {
        return getNombre()+" "+getApellido();
    }

    public void setNivelUser(String id) {
        editor.putString(KEY_NIVEL, id);
        editor.apply();
        editor.commit();
    }

    public String getNivelUser() {
        return pref.getString(KEY_NIVEL, Constants.NIVEL_ESTUDIANTE);
    }

    public void setLastItemId(int itemId) {
        editor.putInt(KEY_ITEM, itemId);
        editor.apply();
        editor.commit();
    }

    public int getLastItemId() {
        return pref.getInt(KEY_ITEM, R.id.todas);
    }

    public String getDescripcionNivel() {
        return pref.getString(KEY_NIVEL_DES, "");
    }

    public void setDescripcionNivel(String val) {
        editor.putString(KEY_NIVEL_DES, val);
        editor.apply();
        editor.commit();
    }
}