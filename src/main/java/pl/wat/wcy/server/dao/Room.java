package pl.wat.wcy.server.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wat.wcy.server.converter.StringSetConverter;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotelId", referencedColumnName = "id")
    private Hotel hotel;
    private String name;
    private String description;
    private Double price;
    @Convert(converter = StringSetConverter.class)
    private Set<String> features;
}
