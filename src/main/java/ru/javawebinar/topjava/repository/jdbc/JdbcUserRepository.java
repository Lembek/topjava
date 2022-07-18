package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.EntityValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RoleExtractor ROLE_EXTRACTOR = new RoleExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final SimpleJdbcInsert insertRole;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.insertRole = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_roles");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        EntityValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            user.getRoles().forEach(role -> saveRole(user.getId(), role));
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) + updateRole(user) == 0) {
            return null;
        }
        return user;
    }

    private int updateRole(User user) {
        int userId = user.getId();
        Map<Integer, List<Role>> map = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", ROLE_EXTRACTOR, userId);
        Set<Role> newRoles = user.getRoles();
        List<Role> oldRoles = map.get(userId);
        if (oldRoles == null) {
            return newRoles.stream().mapToInt((role -> saveRole(userId, role))).sum();
        }
        int difference = newRoles.size() - oldRoles.size();
        switch (difference) {
            case 0:
                if (!newRoles.contains(oldRoles.get(0))) {
                    return removeRole(userId, oldRoles.get(0)) +
                            saveRole(userId, newRoles.stream().findFirst().get());
                }
            case 1:
                return newRoles.stream().filter(role -> !oldRoles.contains(role))
                        .mapToInt((role -> saveRole(userId, role))).sum();
            case -1:
                return oldRoles.stream().filter(role -> !newRoles.contains(role))
                        .mapToInt((role -> removeRole(userId, role))).sum();
        }
        return 0;
    }

    private int saveRole(int userId, Role role) {
        Map<String, Object> args = new HashMap<>();
        args.put("user_id", userId);
        args.put("role", role);
        return insertRole.execute(args);
    }

    private int removeRole(int userId, Role role) {
        return jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=? AND role=?", userId, role.toString());
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return (jdbcTemplate.update("DELETE FROM users WHERE id=?", id) +
                jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id)) != 0;
    }

    @Override
    public User get(int id) {
        return DataAccessUtils.singleResult(getWithRoles("SELECT * FROM users u WHERE id=" + id,
                "SELECT * FROM user_roles WHERE user_id=" + id));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(getWithRoles("SELECT * FROM users WHERE email='" + email + "'",
                "SELECT * FROM user_roles"));
    }

    @Override
    public List<User> getAll() {
        return getWithRoles("SELECT * FROM users ORDER BY name, email", "SELECT * FROM user_roles");
    }

    private List<User> getWithRoles(String userQuery, String roleQuery) {
        List<User> users = jdbcTemplate.query(userQuery, ROW_MAPPER);
        Map<Integer, List<Role>> map = jdbcTemplate.query(roleQuery, ROLE_EXTRACTOR);
        users.forEach(user -> user.setRoles(map.get(user.getId())));
        return users;
    }

    public static class RoleExtractor implements ResultSetExtractor<Map<Integer, List<Role>>> {
        @Override
        public Map<Integer, List<Role>> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, List<Role>> map = new HashMap<>();
            while (rs.next()) {
                map.merge(rs.getInt("user_id"), new ArrayList<>(List.of(Role.valueOf(rs.getString("role")))),
                        (oldValue, newValue) -> {
                            oldValue.addAll(newValue);
                            return oldValue;
                        });
            }
            return map;
        }
    }
}
