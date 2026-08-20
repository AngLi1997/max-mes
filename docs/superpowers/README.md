# Superpowers — 设计蓝图与执行方案

> 沿用 Neta-monorepo 的 `docs/superpowers/` 约定。
> 本目录存放**面向具体需求/迭代的设计文档、实施计划、排查记录**——是「随时间推进的工作流水」，
> 与 `docs/code-wiki/`（沉淀下来的「当前真相」知识库）互补。

## 与 code-wiki 的分工

| | `code-wiki/` | `superpowers/`（本目录） |
|---|---|---|
| 内容 | 当前架构/模块/服务的稳定真相 | 某次需求的设计、计划、排查、复盘 |
| 时效 | 长期维护、随代码更新 | 按日期归档，写完基本不变 |
| 命名 | `<topic>.md` | `YYYY-MM-DD-<feature>-<type>.md` |
| 读者动机 | "这个模块现在长什么样" | "当时为什么这么做 / 这个需求怎么落地" |

## 目录约定

```
superpowers/
├── specs/      # 设计规范：YYYY-MM-DD-<feature>-design.md
├── plans/      # 实施计划/任务拆解：YYYY-MM-DD-<feature>.md
├── followups/  # 验收/E2E 报告：YYYY-MM-DD-<feature>-report.md
└── notes/      # 排查/诊断记录：YYYY-MM-DD-<topic>-diagnosis.md
```

## 使用流程（新需求 / Bug 修复）

1. 设计阶段：在 `specs/` 写 `YYYY-MM-DD-<feature>-design.md`
2. 拆解阶段：在 `plans/` 写对应实施计划
3. 实施完成：必要时在 `code-wiki/` 更新受影响的实体/概念页，并在 `code-wiki/log.md` 记录
4. 复盘/排查：报告进 `followups/`，诊断进 `notes/`

> 当前为目录占位说明，具体文档随实际需求逐步产生。
