local function dumpReagents(skillId)
	local dump = {};
	for reagentId = 1, GetTradeSkillNumReagents(skillId) do
		dump[reagentId] = { GetTradeSkillReagentInfo(skillId, reagentId) };
	end;
	return dump;
end

local function dumpSkills() 
	local dump = {};
	for skillId = 1, GetNumTradeSkills() do
		local skill = {};
		skill['info'] = { GetTradeSkillInfo(skillId) };
		skill['reagents'] = dumpReagents(skillId);
		dump[skillId] = skill;
	end
	return dump;
end

local function needsReagent(reagentsDump, reagentName)
	for k,v in pairs(reagentsDump) do
		if v[1] == reagentName then 
			return true; 
		end
	end
	return false;
end

local function findSkills(skillsDump, reagentName)
	local skills = {}
	for k,v in pairs(skillsDump) do
		if needsReagent(v['reagents'], reagentName) then
			skills[k] = v['info'];
		end
	end
	return skills;
end

local function updateDump()
	if IsTradeSkillLinked() then return; end
	local tradeSkillName, currentLevel, maxLevel = GetTradeSkillLine();
	if not Craftable_SkillDump then Craftable_SkillDump = {}; end
	Craftable_SkillDump[tradeSkillName] = dumpSkills();
end

local colors = {
	optimal = { r = 1.00, g = 0.50, b = 0.25 },
	medium = { r = 1.00, g = 1.00, b = 0.00 },
	easy = { r = 0.25, g = 0.75, b = 0.25 },
	trivial = { r = 0.50, g = 0.50, b = 0.50 },
}

local function append(tooltip, item, prefix, seen)
	if not seen then seen = {}; end
	for skill,dump in pairs(Craftable_SkillDump or {}) do
		for k,v in pairs(findSkills(dump, item)) do
			local n = v[1];
			if not seen[n] then
				local f = colors[ v[2] ];
				tooltip:AddLine(prefix .. '[' .. n .. ']', f['r'], f['g'], f['b']);
				seen[n] = 1;
				seen = append(tooltip, n, prefix .. '   ', seen);
			end
		end
	end
	return seen;
end

local frame = CreateFrame('FRAME');
frame:RegisterEvent('TRADE_SKILL_SHOW');
frame:SetScript('OnEvent', function(self, ...)
	if event == 'TRADE_SKILL_SHOW' then updateDump(); end
end)

local real_GameTooltipOnShow = GameTooltip:GetScript('OnShow')
GameTooltip:SetScript('OnShow', function(self, ...)
	if not self then self = GameTooltip; end
	local item = self.GetItem and self:GetItem()
	if item then append(self, item, ''); end
	if real_GameTooltipOnShow then return real_GameTooltipOnShow(self, ...); end
end)