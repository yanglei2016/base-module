package com.yang.common.tools.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yang.common.tools.pdf.DataProvideVo;
import com.yang.common.tools.pdf.TemplateVo;
import com.yang.common.tools.pdf.entity.BaseNodeVo;
import com.yang.common.tools.pdf.entity.CellNodeVo;
import com.yang.common.tools.pdf.entity.ForEachNodeVo;
import com.yang.common.tools.pdf.entity.IfNodeVo;
import com.yang.common.tools.pdf.entity.ImageNodeVo;
import com.yang.common.tools.pdf.entity.PNodeVo;
import com.yang.common.tools.pdf.entity.TableNodeVo;

public class TemplateEdit {
	private static final Logger logger = LoggerFactory.getLogger(TemplateEdit.class);

	public static void updateTemplate(TemplateVo templateVo, DataProvideVo data) {
		if (templateVo == null || data == null) {
			return;
		}

		List<BaseNodeVo> listNodes = templateVo.getXmlNodeList();
		for (BaseNodeVo baseNodeVo : listNodes) {
			updateNode(baseNodeVo, data.getAllData());
		}
	}

	private static void updateNode(BaseNodeVo baseNodeVo, HashMap<String, Object> data) {
		if (baseNodeVo instanceof PNodeVo) {
			PNodeVo pNodeVo = (PNodeVo) baseNodeVo;
			List<String> allVariables = pNodeVo.getAllVariables();
			if (allVariables != null && allVariables.size() != 0) {
				for (String string : allVariables) {
					String replaceTemp = "${" + string + "}";

					Object obj = data.get(string);
					String value = null;
					if (obj instanceof String) {
						value = (String) obj;
					}

					if (value == null) {
						continue;
					}

					pNodeVo.setContent(pNodeVo.getContent().replace(replaceTemp, value));
				}
			}

			ForEachNodeVo forEachNodeVo = pNodeVo.getForEachNodeVo();
			if (forEachNodeVo != null) {
				updatePForEachNode(pNodeVo, data);
			}

		} else if (baseNodeVo instanceof TableNodeVo) {
			TableNodeVo tableNodeVo = (TableNodeVo) baseNodeVo;
			List<CellNodeVo> listCellNode = tableNodeVo.getCellNodes();
			if (listCellNode != null) {
				for (CellNodeVo cellNodeVo : listCellNode) {
					updateNode(cellNodeVo, data);
				}
			}

			// 更新foreach
			updateTableForEachNode(tableNodeVo, data);

		} else if (baseNodeVo instanceof CellNodeVo) {
			CellNodeVo cellNodeVo = (CellNodeVo) baseNodeVo;
			PNodeVo pNodeVo = cellNodeVo.getpNodeVo();
			if (pNodeVo != null) {
				updateNode(pNodeVo, data);
			}

			TableNodeVo tableNodeVo = cellNodeVo.getTableNodeVo();
			if (tableNodeVo != null) {
				updateNode(tableNodeVo, data);
			}

			List<IfNodeVo> listIf = cellNodeVo.getListIf();
			if (listIf != null) {
				for (IfNodeVo ifNodeVo : listIf) {
					if (updateForIfNode(ifNodeVo, data)) {
						BaseNodeVo tmpVo = ifNodeVo.getBaseNodeVo();
						if (tmpVo instanceof PNodeVo) {
							cellNodeVo.setpNodeVo((PNodeVo) tmpVo);
							break;
						}

						if (tmpVo instanceof TableNodeVo) {
							cellNodeVo.setTableNodeVo((TableNodeVo) tmpVo);
							break;
						}
					}
				}
			}

		} else if (baseNodeVo instanceof ForEachNodeVo) {
			ForEachNodeVo forEachNodeVo = (ForEachNodeVo) baseNodeVo;
			List<CellNodeVo> listCellNode = forEachNodeVo.getCellNodes();
			if (listCellNode != null) {
				for (CellNodeVo cellNodeVo : listCellNode) {
					updateNode(cellNodeVo, data);
				}
			}

			String content = forEachNodeVo.getContent();
			if (content != null && !content.trim().isEmpty()) {
				List<String> allVariables = forEachNodeVo.getContentVar();
				if (allVariables != null && allVariables.size() != 0) {
					for (String string : allVariables) {
						String replaceTemp = "${" + string + "}";

						Object obj = data.get(string);
						String value = null;
						if (obj instanceof String) {
							value = (String) obj;
						}

						if (value == null) {
							continue;
						}

						forEachNodeVo.setContent(forEachNodeVo.getContent().replace(replaceTemp, value));
					}
				}
			}
		} else if (baseNodeVo instanceof ImageNodeVo) {
			ImageNodeVo imageNodeVo = (ImageNodeVo) baseNodeVo;
			List<String> allVariables = imageNodeVo.getAllVariables();
			if (allVariables != null && allVariables.size() != 0) {
				for (String string : allVariables) {
					String replaceTemp = "${" + string + "}";

					Object obj = data.get(string);
					String value = null;
					if (obj instanceof String) {
						value = (String) obj;
					}

					if (value == null) {
						continue;
					}

					imageNodeVo.setSrc(imageNodeVo.getSrc().replace(replaceTemp, value));
				}
			}
		}
	}

	private static void updateTableForEachNode(TableNodeVo tableNodeVo, HashMap<String, Object> data) {
		ForEachNodeVo forEachNodeVo = tableNodeVo.getForEachNodeVo();
		if (forEachNodeVo == null) {
			return;
		}
		List<String> inputVar = forEachNodeVo.getInputVar();

		String items = forEachNodeVo.getItems();
		Object object = data.get(items);
		if (object instanceof List) {
			try {
				List<ForEachNodeVo> result = new ArrayList<ForEachNodeVo>();
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) object;

				for (HashMap<String, Object> hashMap : list) {
					if (inputVar != null && inputVar.size() != 0) {
						for (String var : inputVar) {
							hashMap.put(var, data.get(var));
						}
					}

					ForEachNodeVo cloneNode = (ForEachNodeVo) forEachNodeVo.deepClone();
					updateNode(cloneNode, hashMap);
					result.add(cloneNode);
				}

				tableNodeVo.setListForEachNodeVos(result);
			} catch (ClassNotFoundException | IOException e) {
				logger.error("spreadTableForEachNode 异常", e);
			}
		}
	}

	private static void updatePForEachNode(PNodeVo pNodeVo, HashMap<String, Object> data) {
		ForEachNodeVo forEachNodeVo = pNodeVo.getForEachNodeVo();
		if (forEachNodeVo == null) {
			return;
		}

		List<String> inputVar = forEachNodeVo.getInputVar();

		String items = forEachNodeVo.getItems();
		Object object = data.get(items);
		if (object instanceof List) {
			try {
				List<ForEachNodeVo> result = new ArrayList<ForEachNodeVo>();
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) object;

				for (HashMap<String, Object> hashMap : list) {
					ForEachNodeVo cloneNode = (ForEachNodeVo) forEachNodeVo.deepClone();

					if (inputVar != null && inputVar.size() != 0) {
						for (String var : inputVar) {
							hashMap.put(var, data.get(var));
						}
					}

					updateNode(cloneNode, hashMap);
					result.add(cloneNode);
				}

				pNodeVo.setListForEachNodeVos(result);
			} catch (ClassNotFoundException | IOException e) {
				logger.error("spreadTableForEachNode 异常", e);
			}
		}
	}

	private static boolean updateForIfNode(IfNodeVo ifNodeVo, HashMap<String, Object> data) {
		if (ifNodeVo == null) {
			return false;
		}

		String test = ifNodeVo.getTest();
		Object obj = data.get(test);
		String value = null;
		if (obj instanceof String) {
			value = (String) obj;
		}

		if (value == null) {
			return false;
		}

		if (value.equals(ifNodeVo.getEquals())) {
			updateNode(ifNodeVo.getBaseNodeVo(), data);
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {

	}

}
