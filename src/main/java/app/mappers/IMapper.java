package app.mappers;

public interface IMapper <E, Q, U, R>{
    E createDTOToEntity(Q dto);
    E updateDTOToEntity(U dto);
    R entityToDTO(E entity);
}
