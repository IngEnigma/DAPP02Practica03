package org.uv.dapp02practica03;

import java.util.List;

public interface IDAOGeneral <T, ID> {
    public T guardar(T pojo);
    public T modificar(T pojo, ID id);
    public boolean eliminar(ID id);
    
    public T buscarById(ID id);
    public List<T> buscarAll();
}
