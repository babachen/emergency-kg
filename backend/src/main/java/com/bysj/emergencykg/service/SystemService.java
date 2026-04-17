package com.bysj.emergencykg.service;

import com.bysj.emergencykg.common.PageResult;
import com.bysj.emergencykg.model.dto.SystemDTO;
import com.bysj.emergencykg.model.entity.SysUser;
import com.bysj.emergencykg.model.vo.SystemVO;

import java.util.List;
import java.util.Set;

public interface SystemService {
    PageResult<SystemVO.UserVO> pageUsers(SystemDTO.UserQueryDTO queryDTO);
    void saveUser(SystemDTO.UserSaveDTO dto);
    void updateUser(Long id, SystemDTO.UserSaveDTO dto);
    void deleteUser(Long id);
    List<SystemVO.RoleVO> listRoles();
    void saveRole(SystemDTO.RoleSaveDTO dto);
    void updateRole(Long id, SystemDTO.RoleSaveDTO dto);
    void deleteRole(Long id);
    List<SystemVO.MenuVO> listMenus();
    List<SystemVO.MenuVO> getCurrentUserMenus(Long userId);
    void saveMenu(SystemDTO.MenuSaveDTO dto);
    void updateMenu(Long id, SystemDTO.MenuSaveDTO dto);
    void deleteMenu(Long id);
    PageResult<SystemVO.LogVO> pageLogs(SystemDTO.LogQueryDTO queryDTO);
    List<SystemVO.DictVO> listDictData(String dictCode);
    SysUser getUserById(Long userId);
    Set<String> getPermissionCodes(Long userId);
    boolean isAdmin(Long userId);
    List<String> getRoleCodes(Long userId);
    List<String> getRoleNames(Long userId);
    SystemVO.CurrentUserVO buildCurrentUser(Long userId);
}
