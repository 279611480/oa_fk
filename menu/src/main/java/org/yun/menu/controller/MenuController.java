package org.yun.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yun.common.data.domain.Result;
import org.yun.identity.domain.Role;
import org.yun.identity.service.RoleService;
import org.yun.menu.domain.Menu;
import org.yun.menu.service.MenuService;

//http://127.0.0.1:8080/menu
@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("/menu/index");
		
		List<Role> roles = this.roleService.findAll();
		/*roles.forEach(r->{
			System.out.println("r.getName()--------->"+r.getName());
		});
		*/
		mav.addObject("roles",roles);
		//System.out.println("mav---------->" + mav.getModelMap() + mav );
		return mav;
	}
	
	
	//加上produces="application/json"
	//表示  将返回的数据类型改为JSON格式    不然会报错  因为URL与 上面的接收请求相同
	// 如果客户端要求返回JSON的时候，调用下面这个方法
	//不写的话  会报错因为与上面接收请求的URL相同了
	//虽然他们URL相同 ，但是接收请求返回的类型不同  所以需要写上produces
	@GetMapping(produces="application/json")
	@ResponseBody
	public List<Menu> findTopMenus(){
		return this.menuService.findTopMenus();
	}
	
	
	
	@PostMapping
	public String save(Menu menu) {
		this.menuService.save(menu);
		
		return "redirect:/menu";
	} 
	
	
	@PostMapping("move")
	@ResponseBody
	public Result move(String id,String targetId,String moveType) {
		return this.menuService.move(id,targetId,moveType);
	}
	
	@DeleteMapping("{id}")
	@ResponseBody
	public Result delete(@PathVariable("id") String id) {
		return this.menuService.delete(id);
	}
	
	// 返回JSON，使用AJAX来获取
	@GetMapping(value="menus",produces="application/json")
	@ResponseBody
	 public List<Menu> findMyMenus(){
		//找当前的用户
		
		
		//TODO 当前暂时没有用户，所以直接查询所有菜单
		return this.menuService.findTopMenus();
	}
	
}
