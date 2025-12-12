package app.mappers;

public interface IMapper <E, D>{
    E dtoToEntity(D dto);
    D entityToDTO(E entity);
}
