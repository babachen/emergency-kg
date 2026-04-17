package com.bysj.emergencykg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bysj.emergencykg.common.PageResult;
import com.bysj.emergencykg.exception.BusinessException;
import com.bysj.emergencykg.mapper.*;
import com.bysj.emergencykg.model.dto.SystemDTO;
import com.bysj.emergencykg.model.entity.*;
import com.bysj.emergencykg.model.vo.SystemVO;
import com.bysj.emergencykg.service.SystemService;
import com.bysj.emergencykg.utils.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SystemServiceImpl implements SystemService {
    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysOperationLogMapper sysOperationLogMapper;
    private final SysDictDataMapper sysDictDataMapper;
    private final PasswordUtils passwordUtils;

    public SystemServiceImpl(SysUserMapper sysUserMapper, SysRoleMapper sysRoleMapper, SysMenuMapper sysMenuMapper,
                             SysUserRoleMapper sysUserRoleMapper, SysRoleMenuMapper sysRoleMenuMapper,
                             SysOperationLogMapper sysOperationLogMapper, SysDictDataMapper sysDictDataMapper,
                             PasswordUtils passwordUtils) {
        this.sysUserMapper = sysUserMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysMenuMapper = sysMenuMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMenuMapper = sysRoleMenuMapper;
        this.sysOperationLogMapper = sysOperationLogMapper;
        this.sysDictDataMapper = sysDictDataMapper;
        this.passwordUtils = passwordUtils;
    }

    @Override
    public PageResult<SystemVO.UserVO> pageUsers(SystemDTO.UserQueryDTO queryDTO) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .and(StringUtils.hasText(queryDTO.getKeyword()), w -> w.like(SysUser::getUsername, queryDTO.getKeyword()).or().like(SysUser::getRealName, queryDTO.getKeyword()))
                .eq(queryDTO.getStatus() != null, SysUser::getStatus, queryDTO.getStatus())
                .orderByDesc(SysUser::getCreateTime);
        Page<SysUser> page = sysUserMapper.selectPage(new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize()), wrapper);
        return PageResult.of(page, this::toUserVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SystemDTO.UserSaveDTO dto) {
        if (existsUserName(dto.getUsername(), null)) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordUtils.encode(StringUtils.hasText(dto.getPassword()) ? dto.getPassword() : "123456"));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setAvatar(dto.getAvatar());
        user.setRemark(dto.getRemark());
        user.setStatus(dto.getStatus());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.insert(user);
        saveUserRoles(user.getId(), dto.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long id, SystemDTO.UserSaveDTO dto) {
        SysUser user = requireUser(id);
        if (existsUserName(dto.getUsername(), id)) {
            throw new BusinessException("用户名已存在");
        }
        user.setUsername(dto.getUsername());
        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordUtils.encode(dto.getPassword()));
        }
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setAvatar(dto.getAvatar());
        user.setRemark(dto.getRemark());
        user.setStatus(dto.getStatus());
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        saveUserRoles(id, dto.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        if (Objects.equals(id, 1L)) {
            throw new BusinessException("默认管理员不能删除");
        }
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        sysUserMapper.deleteById(id);
    }

    @Override
    public List<SystemVO.RoleVO> listRoles() {
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getId))
                .stream().map(this::toRoleVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SystemDTO.RoleSaveDTO dto) {
        if (existsRoleCode(dto.getRoleCode(), null)) {
            throw new BusinessException("角色编码已存在");
        }
        SysRole role = new SysRole();
        role.setRoleCode(dto.getRoleCode());
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setStatus(dto.getStatus());
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        sysRoleMapper.insert(role);
        saveRoleMenus(role.getId(), dto.getMenuIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Long id, SystemDTO.RoleSaveDTO dto) {
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        if (existsRoleCode(dto.getRoleCode(), id)) {
            throw new BusinessException("角色编码已存在");
        }
        role.setRoleCode(dto.getRoleCode());
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setStatus(dto.getStatus());
        role.setUpdateTime(LocalDateTime.now());
        sysRoleMapper.updateById(role);
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        saveRoleMenus(id, dto.getMenuIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        if (Objects.equals(id, 1L)) {
            throw new BusinessException("默认管理员角色不能删除");
        }
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
        sysRoleMapper.deleteById(id);
    }

    @Override
    public List<SystemVO.MenuVO> listMenus() {
        return buildMenuTree(sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder).orderByAsc(SysMenu::getId)));
    }

    @Override
    public List<SystemVO.MenuVO> getCurrentUserMenus(Long userId) {
        List<SysMenu> allMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getStatus, 1)
                .eq(SysMenu::getVisible, 1)
                .orderByAsc(SysMenu::getSortOrder).orderByAsc(SysMenu::getId));
        if (isAdmin(userId)) {
            return buildMenuTree(allMenus.stream().filter(m -> !"BUTTON".equalsIgnoreCase(m.getMenuType())).collect(Collectors.toList()));
        }
        Set<Long> menuIds = getRoleMenuIds(getRoleIds(userId));
        return buildMenuTree(allMenus.stream().filter(m -> menuIds.contains(m.getId()) && !"BUTTON".equalsIgnoreCase(m.getMenuType())).collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(SystemDTO.MenuSaveDTO dto) {
        SysMenu menu = new SysMenu();
        copyMenu(dto, menu);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        sysMenuMapper.insert(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Long id, SystemDTO.MenuSaveDTO dto) {
        SysMenu menu = sysMenuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }
        copyMenu(dto, menu);
        menu.setUpdateTime(LocalDateTime.now());
        sysMenuMapper.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        Long childCount = sysMenuMapper.selectCount(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (childCount > 0) {
            throw new BusinessException("请先删除子菜单");
        }
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, id));
        sysMenuMapper.deleteById(id);
    }

    @Override
    public PageResult<SystemVO.LogVO> pageLogs(SystemDTO.LogQueryDTO queryDTO) {
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<SysOperationLog>()
                .like(StringUtils.hasText(queryDTO.getModuleName()), SysOperationLog::getModuleName, queryDTO.getModuleName())
                .like(StringUtils.hasText(queryDTO.getOperationType()), SysOperationLog::getOperationType, queryDTO.getOperationType())
                .orderByDesc(SysOperationLog::getCreateTime);
        Page<SysOperationLog> page = sysOperationLogMapper.selectPage(new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize()), wrapper);
        return PageResult.of(page, log -> {
            SystemVO.LogVO vo = new SystemVO.LogVO();
            vo.setId(log.getId());
            vo.setModuleName(log.getModuleName());
            vo.setOperationType(log.getOperationType());
            vo.setOperatorName(log.getOperatorName());
            vo.setContent(log.getContent());
            vo.setIpAddress(log.getIpAddress());
            vo.setCreateTime(log.getCreateTime());
            return vo;
        });
    }

    @Override
    public List<SystemVO.DictVO> listDictData(String dictCode) {
        return sysDictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getDictTypeCode, dictCode).orderByAsc(SysDictData::getSortOrder))
                .stream().map(item -> {
                    SystemVO.DictVO vo = new SystemVO.DictVO();
                    vo.setId(item.getId());
                    vo.setDictTypeCode(item.getDictTypeCode());
                    vo.setDictLabel(item.getDictLabel());
                    vo.setDictValue(item.getDictValue());
                    vo.setCssType(item.getCssType());
                    vo.setSortOrder(item.getSortOrder());
                    vo.setStatus(item.getStatus());
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public SysUser getUserById(Long userId) {
        return sysUserMapper.selectById(userId);
    }

    @Override
    public Set<String> getPermissionCodes(Long userId) {
        if (isAdmin(userId)) {
            return sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().isNotNull(SysMenu::getPermissionCode))
                    .stream().map(SysMenu::getPermissionCode).filter(StringUtils::hasText).collect(Collectors.toSet());
        }
        Set<Long> menuIds = getRoleMenuIds(getRoleIds(userId));
        if (menuIds.isEmpty()) {
            return Collections.emptySet();
        }
        return sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().in(SysMenu::getId, menuIds).isNotNull(SysMenu::getPermissionCode))
                .stream().map(SysMenu::getPermissionCode).filter(StringUtils::hasText).collect(Collectors.toSet());
    }

    @Override
    public boolean isAdmin(Long userId) {
        return getRoleCodes(userId).contains("admin");
    }

    @Override
    public List<String> getRoleCodes(Long userId) {
        return loadRoles(userId).stream().map(SysRole::getRoleCode).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleNames(Long userId) {
        return loadRoles(userId).stream().map(SysRole::getRoleName).collect(Collectors.toList());
    }

    @Override
    public SystemVO.CurrentUserVO buildCurrentUser(Long userId) {
        SysUser user = requireUser(userId);
        SystemVO.CurrentUserVO vo = new SystemVO.CurrentUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(user.getStatus());
        vo.setRoleCodes(getRoleCodes(userId));
        vo.setRoleNames(getRoleNames(userId));
        vo.setAdmin(isAdmin(userId));
        return vo;
    }

    private SysUser requireUser(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    private boolean existsUserName(String username, Long excludeId) {
        return sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username).ne(excludeId != null, SysUser::getId, excludeId)) > 0;
    }

    private boolean existsRoleCode(String roleCode, Long excludeId) {
        return sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, roleCode).ne(excludeId != null, SysRole::getId, excludeId)) > 0;
    }

    private void saveUserRoles(Long userId, List<Long> roleIds) {
        if (roleIds == null) {
            return;
        }
        for (Long roleId : roleIds) {
            SysUserRole relation = new SysUserRole();
            relation.setUserId(userId);
            relation.setRoleId(roleId);
            sysUserRoleMapper.insert(relation);
        }
    }

    private void saveRoleMenus(Long roleId, List<Long> menuIds) {
        if (menuIds == null) {
            return;
        }
        for (Long menuId : menuIds) {
            SysRoleMenu relation = new SysRoleMenu();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            sysRoleMenuMapper.insert(relation);
        }
    }

    private List<Long> getRoleIds(Long userId) {
        return sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    private List<SysRole> loadRoles(Long userId) {
        List<Long> roleIds = getRoleIds(userId);
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().in(SysRole::getId, roleIds));
    }

    private Set<Long> getRoleMenuIds(List<Long> roleIds) {
        if (roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        return sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds)).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
    }

    private SystemVO.UserVO toUserVO(SysUser user) {
        SystemVO.UserVO vo = new SystemVO.UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(user.getStatus());
        vo.setStatusText(user.getStatus() != null && user.getStatus() == 1 ? "启用" : "停用");
        vo.setRemark(user.getRemark());
        vo.setLastLoginTime(user.getLastLoginTime());
        vo.setCreateTime(user.getCreateTime());
        vo.setRoleIds(getRoleIds(user.getId()));
        vo.setRoleNames(getRoleNames(user.getId()));
        return vo;
    }

    private SystemVO.RoleVO toRoleVO(SysRole role) {
        SystemVO.RoleVO vo = new SystemVO.RoleVO();
        vo.setId(role.getId());
        vo.setRoleCode(role.getRoleCode());
        vo.setRoleName(role.getRoleName());
        vo.setDescription(role.getDescription());
        vo.setStatus(role.getStatus());
        vo.setStatusText(role.getStatus() != null && role.getStatus() == 1 ? "启用" : "停用");
        vo.setMenuIds(sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, role.getId())).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList()));
        return vo;
    }

    private List<SystemVO.MenuVO> buildMenuTree(List<SysMenu> menus) {
        Map<Long, SystemVO.MenuVO> map = menus.stream().map(this::toMenuVO).collect(Collectors.toMap(SystemVO.MenuVO::getId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        List<SystemVO.MenuVO> roots = new ArrayList<>();
        for (SystemVO.MenuVO menu : map.values()) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                roots.add(menu);
            } else if (map.containsKey(menu.getParentId())) {
                map.get(menu.getParentId()).getChildren().add(menu);
            }
        }
        return roots;
    }

    private SystemVO.MenuVO toMenuVO(SysMenu menu) {
        SystemVO.MenuVO vo = new SystemVO.MenuVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setMenuName(menu.getMenuName());
        vo.setMenuType(menu.getMenuType());
        vo.setRoutePath(menu.getRoutePath());
        vo.setComponentPath(menu.getComponentPath());
        vo.setIcon(menu.getIcon());
        vo.setPermissionCode(menu.getPermissionCode());
        vo.setSortOrder(menu.getSortOrder());
        vo.setVisible(menu.getVisible());
        vo.setStatus(menu.getStatus());
        return vo;
    }

    private void copyMenu(SystemDTO.MenuSaveDTO dto, SysMenu menu) {
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setParentId(dto.getParentId());
        menu.setRoutePath(dto.getRoutePath());
        menu.setComponentPath(dto.getComponentPath());
        menu.setIcon(dto.getIcon());
        menu.setPermissionCode(dto.getPermissionCode());
        menu.setSortOrder(dto.getSortOrder());
        menu.setVisible(dto.getVisible());
        menu.setStatus(dto.getStatus());
    }
}
